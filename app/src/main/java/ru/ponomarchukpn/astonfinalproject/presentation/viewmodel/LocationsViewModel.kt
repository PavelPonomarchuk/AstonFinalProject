package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationsFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetLocationsFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetLocationsUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.LoadLocationsPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.SaveLocationsFilterUseCase
import javax.inject.Inject

class LocationsViewModel @Inject constructor(
    private val saveLocationsFilterUseCase: SaveLocationsFilterUseCase,
    private val getLocationsFilterUseCase: GetLocationsFilterUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val loadLocationsPageUseCase: LoadLocationsPageUseCase,
    private val pageHolder: LocationsPageHolder,
    private val matcher: LocationsMatcher
) : ViewModel() {

    private val _locationsListState = MutableSharedFlow<List<LocationEntity>>(1)
    val locationsListState = _locationsListState.asSharedFlow()

    private val _notEmptyFilterState = MutableStateFlow<Boolean?>(null)
    val notEmptyFilterState = _notEmptyFilterState.asStateFlow()
        .filterNotNull()

    private val _errorState = MutableStateFlow<Any?>(null)
    val errorState = _errorState.asStateFlow()
        .filterNotNull()

    private val _emptyResultState = MutableStateFlow<Any?>(null)
    val emptyResultState = _emptyResultState.asStateFlow()
        .filterNotNull()

    private var job: Job? = null
    private val emptyFilterSettings = LocationsFilterSettings(
        EMPTY_STRING, EMPTY_STRING, EMPTY_STRING
    )
    private var searchQuery = EMPTY_STRING

    fun onViewCreated() {
        if (pageHolder.currentPageNumber() == INITIAL_PAGE_NUMBER) {
            loadPage()
        }
        provideLocationsFlow()
        loadFilterIsPresent()
    }

    fun onListEnded() {
        loadPage()
    }

    fun onListSwiped() {
        resetData()
    }

    fun onFilterSettingsChanged() {
        resetData()
    }

    fun onButtonClearPressed() {
        viewModelScope.launch(Dispatchers.IO) {
            val emptySettingsSaved = saveLocationsFilterUseCase.invoke(emptyFilterSettings)
            if (emptySettingsSaved) {
                resetData()
                _notEmptyFilterState.tryEmit(false)
            }
        }
    }

    fun onSearchQueryChanged(query: String?) {
        searchQuery = query?.trim() ?: EMPTY_STRING
        resetData()
    }

    private fun provideLocationsFlow() {
        job = viewModelScope.launch(Dispatchers.IO) {
            getLocationsUseCase.invoke()
                .catch {
                    emitError()
                }
                .collect { locationsList ->
                    val filter = getLocationsFilterUseCase.invoke()
                    val filtered = locationsList.filter { matcher.isLocationMatches(filter, it) }
                    emitFilteredWithQuery(filtered)
                }
        }
    }

    private fun emitFilteredWithQuery(locations: List<LocationEntity>) {
        if (searchQuery != EMPTY_STRING) {
            locations.filter {
                it.name.contains(searchQuery, true)
            }.also {
                _locationsListState.tryEmit(it)
                if (it.isEmpty()) {
                    emitEmptyResultState()
                }
            }
        } else {
            _locationsListState.tryEmit(locations)
            if (locations.isEmpty()) {
                emitEmptyResultState()
            }
        }
    }

    private fun emitError() {
        _errorState.tryEmit(Any())
    }

    private fun emitEmptyResultState() {
        _emptyResultState.tryEmit(Any())
    }

    private fun resetData() {
        job?.cancel()
        provideLocationsFlow()
    }

    private fun loadPage() {
        viewModelScope.launch(Dispatchers.IO) {
            var pageNumber = pageHolder.currentPageNumber()
            val success = loadLocationsPageUseCase.invoke(pageNumber)
            if (success) {
                pageNumber++
                pageHolder.savePageNumber(pageNumber)
            } else {
                emitError()
            }
        }
    }

    private fun loadFilterIsPresent() {
        viewModelScope.launch(Dispatchers.IO) {
            val filterSettings = getLocationsFilterUseCase.invoke()
            if (filterSettings != emptyFilterSettings) {
                _notEmptyFilterState.tryEmit(true)
            } else {
                _notEmptyFilterState.tryEmit(false)
            }
        }
    }

    companion object {

        private const val EMPTY_STRING = ""
        private const val INITIAL_PAGE_NUMBER = 1
    }
}

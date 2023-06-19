package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetLocationsPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.ResetLocationsPageUseCase
import javax.inject.Inject

class LocationsViewModel @Inject constructor(
    private val getLocationsPageUseCase: GetLocationsPageUseCase,
    private val resetLocationsPageUseCase: ResetLocationsPageUseCase
) : ViewModel() {

    private val _locationsListState = MutableStateFlow<List<LocationEntity>?>(null)
    val locationsListState = _locationsListState.asStateFlow()
        .filterNotNull()

    private val locationsList = mutableListOf<LocationEntity>()
    private var searchQuery = EMPTY_QUERY

    fun onViewCreated() {
        nextPage()
    }

    fun onListEnded() {
        nextPage()
    }

    fun onListSwiped() {
        resetLocationsPageUseCase.invoke()
        nextPage()
    }

    fun onFilterSettingsChanged() {
        //TODO обновлять данные
    }

    fun onSearchQueryChanged(query: String?) {
        searchQuery = query ?: EMPTY_QUERY
        emitFilteredWithQuery()
    }

    private fun emitFilteredWithQuery() {
        if (searchQuery != EMPTY_QUERY) {
            locationsList.filter {
                it.name.contains(searchQuery, true)
            }.also {
                _locationsListState.tryEmit(it)
            }
        } else {
            _locationsListState.tryEmit(locationsList.toList())
        }
    }

    //TODO косяк в логике получения данных
    //при переключении табов и возврате назад показывается список со следующей страницы
    //то же при повороте экрана, загружается следующая страница
    private fun nextPage() {
        viewModelScope.launch(Dispatchers.IO) {
            val page = getLocationsPageUseCase.invoke()
            if (page.isNotEmpty()) {
                locationsList.addAll(page)
                emitFilteredWithQuery()
            }
        }
    }

    companion object {

        private const val EMPTY_QUERY = ""
    }
}

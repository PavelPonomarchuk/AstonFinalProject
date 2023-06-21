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
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodesFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetEpisodesFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetEpisodesUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.LoadEpisodesPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.SaveEpisodesFilterUseCase
import javax.inject.Inject

class EpisodesViewModel @Inject constructor(
    private val saveEpisodesFilterUseCase: SaveEpisodesFilterUseCase,
    private val getEpisodesFilterUseCase: GetEpisodesFilterUseCase,
    private val getEpisodesUseCase: GetEpisodesUseCase,
    private val loadEpisodesPageUseCase: LoadEpisodesPageUseCase,
    private val pageHolder: EpisodesPageHolder,
    private val matcher: EpisodesMatcher
) : ViewModel() {

    private val _episodesListState = MutableSharedFlow<List<EpisodeEntity>>(1)
    val episodesListState = _episodesListState.asSharedFlow()

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
    private val emptyFilterSettings = EpisodesFilterSettings(EMPTY_STRING, EMPTY_STRING)
    private var searchQuery = EMPTY_STRING

    fun onViewCreated() {
        if (pageHolder.currentPageNumber() == INITIAL_PAGE_NUMBER) {
            loadPage()
        }
        provideEpisodesFlow()
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
            val emptySettingsSaved = saveEpisodesFilterUseCase.invoke(emptyFilterSettings)
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

    private fun provideEpisodesFlow() {
        job = viewModelScope.launch(Dispatchers.IO) {
            getEpisodesUseCase.invoke()
                .catch {
                    emitError()
                }
                .collect { episodesList ->
                    val filter = getEpisodesFilterUseCase.invoke()
                    val filtered = episodesList.filter { matcher.isEpisodeMatches(filter, it) }
                    emitFilteredWithQuery(filtered)
                }
        }
    }

    private fun emitFilteredWithQuery(episodes: List<EpisodeEntity>) {
        if (searchQuery != EMPTY_STRING) {
            episodes.filter {
                it.name.contains(searchQuery, true)
            }.also {
                _episodesListState.tryEmit(it)
                if (it.isEmpty()) {
                    emitEmptyResultState()
                }
            }
        } else {
            _episodesListState.tryEmit(episodes)
            if (episodes.isEmpty()) {
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
        provideEpisodesFlow()
    }

    private fun loadPage() {
        viewModelScope.launch(Dispatchers.IO) {
            var pageNumber = pageHolder.currentPageNumber()
            val success = loadEpisodesPageUseCase.invoke(pageNumber)
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
            val filterSettings = getEpisodesFilterUseCase.invoke()
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

package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import javax.inject.Inject

class EpisodesViewModel @Inject constructor(
//    private val getEpisodesPageUseCase: GetEpisodesPageUseCase,
//    private val resetEpisodesPageUseCase: ResetEpisodesPageUseCase
) : ViewModel() {

    private val _episodesListState = MutableStateFlow<List<EpisodeEntity>?>(null)
    val episodesListState = _episodesListState.asStateFlow()
        .filterNotNull()

    private val episodesList = mutableListOf<EpisodeEntity>()
    private var searchQuery = EMPTY_QUERY

    fun onViewCreated() {
        nextPage()
    }

    fun onListEnded() {
        nextPage()
    }

    fun onListSwiped() {
//        resetEpisodesPageUseCase.invoke()
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
            episodesList.filter {
                it.name.contains(searchQuery, true)
            }.also {
                _episodesListState.tryEmit(it)
            }
        } else {
            _episodesListState.tryEmit(episodesList.toList())
        }
    }

    //TODO косяк в логике получения данных
    //при переключении табов и возврате назад показывается список со следующей страницы
    //то же при повороте экрана, загружается следующая страница
    private fun nextPage() {
        viewModelScope.launch(Dispatchers.IO) {
//            val page = getEpisodesPageUseCase.invoke()
//            if (page.isNotEmpty()) {
//                episodesList.addAll(page)
//                emitFilteredWithQuery()
//            }
        }
    }

    companion object {

        private const val EMPTY_QUERY = ""
    }
}

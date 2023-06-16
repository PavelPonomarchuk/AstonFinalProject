package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersByIdUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleEpisodeUseCase
import javax.inject.Inject

class EpisodeDetailsViewModel @Inject constructor(
    private val getSingleEpisodeUseCase: GetSingleEpisodeUseCase,
    private val getCharactersByIdUseCase: GetCharactersByIdUseCase
) : ViewModel() {

    private var _episodeState = MutableStateFlow<EpisodeEntity?>(null)
    val episodeState = _episodeState.asStateFlow()
        .filterNotNull()

    private var _charactersListState = MutableStateFlow<List<CharacterEntity>?>(null)
    val charactersListState = _charactersListState.asStateFlow()
        .filterNotNull()

    private var _errorState = MutableStateFlow<Boolean?>(null)
    val errorState = _errorState.asStateFlow()
        .filterNotNull()

    private var entity: EpisodeEntity? = null
    private val charactersList = mutableListOf<CharacterEntity>()

    fun onViewCreated(episodeId: Int) {
        loadEpisode(episodeId)
    }

    private fun loadEpisode(episodeId: Int) {
        viewModelScope.launch {
            val result = getSingleEpisodeUseCase.invoke(episodeId)
            result?.let {
                entity = result
                loadCharacters()
            } ?: emitError()
        }
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            val result = entity?.charactersId?.let { getCharactersByIdUseCase.invoke(it) }
            result?.let {
                charactersList.addAll(it)
                emitData()
            } ?: emitError()
        }
    }

    private fun emitData() {
        _episodeState.tryEmit(entity)
        _charactersListState.tryEmit(charactersList)
    }

    private fun emitError() {
        _errorState.tryEmit(true)
    }
}

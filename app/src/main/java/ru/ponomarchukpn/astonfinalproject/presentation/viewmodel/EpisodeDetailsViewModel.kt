package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersByIdUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleEpisodeUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.LoadCharactersByIdUseCase
import javax.inject.Inject

class EpisodeDetailsViewModel @Inject constructor(
    private val getSingleEpisodeUseCase: GetSingleEpisodeUseCase,
    private val getCharactersByIdUseCase: GetCharactersByIdUseCase,
    private val loadCharactersByIdUseCase: LoadCharactersByIdUseCase
) : ViewModel() {

    private val _episodeState = MutableStateFlow<EpisodeEntity?>(null)
    val episodeState = _episodeState.asStateFlow()
        .filterNotNull()

    private val _charactersListState = MutableStateFlow<List<CharacterEntity>?>(null)
    val charactersListState = _charactersListState.asStateFlow()
        .filterNotNull()

    private val _errorState = MutableStateFlow<Any?>(null)
    val errorState = _errorState.asStateFlow()
        .filterNotNull()

    private var episodeJob: Job? = null
    private var charactersJob: Job? = null

    fun onViewCreated(episodeId: Int) {
        provideEpisodeFlow(episodeId)
    }

    fun onButtonReloadPressed(episodeId: Int) {
        episodeJob?.cancel()
        charactersJob?.cancel()
        provideEpisodeFlow(episodeId)
    }

    private fun provideEpisodeFlow(episodeId: Int) {
        episodeJob = viewModelScope.launch(Dispatchers.IO) {
            getSingleEpisodeUseCase.invoke(episodeId)
                .catch {
                    emitError()
                }
                .collect {
                    _episodeState.tryEmit(it)
                    provideExtraEpisodeDetails(it)
                }
        }
    }

    private fun provideExtraEpisodeDetails(episode: EpisodeEntity) {
        loadCharacters(episode.charactersId)
        provideCharactersFlow(episode.charactersId)
    }

    private fun loadCharacters(ids: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            loadCharactersByIdUseCase.invoke(ids)
        }
    }

    private fun provideCharactersFlow(ids: List<Int>) {
        if (ids.isEmpty()) {
            _charactersListState.tryEmit(emptyList())
            return
        }
        charactersJob = viewModelScope.launch(Dispatchers.IO) {
            getCharactersByIdUseCase.invoke(ids)
                .catch {
                    emitError()
                }
                .collect {
                    _charactersListState.tryEmit(it)
                }
        }
    }

    private fun emitError() {
        _errorState.tryEmit(Any())
    }
}

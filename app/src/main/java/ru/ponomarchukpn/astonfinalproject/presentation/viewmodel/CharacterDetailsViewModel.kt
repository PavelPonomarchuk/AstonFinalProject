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
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetEpisodesByIdUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleCharacterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleLocationUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.LoadEpisodesByIdUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.LoadLocationsByIdUseCase
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val getSingleCharacterUseCase: GetSingleCharacterUseCase,
    private val getEpisodesByIdUseCase: GetEpisodesByIdUseCase,
    private val loadLocationsByIdUseCase: LoadLocationsByIdUseCase,
    private val loadEpisodesByIdUseCase: LoadEpisodesByIdUseCase,
    private val getSingleLocationUseCase: GetSingleLocationUseCase
) : ViewModel() {

    private val _characterState = MutableStateFlow<CharacterEntity?>(null)
    val characterState = _characterState.asStateFlow()
        .filterNotNull()

    private val _originNameState = MutableStateFlow<String?>(null)
    val originNameState = _originNameState.asStateFlow()
        .filterNotNull()

    private val _locationNameState = MutableStateFlow<String?>(null)
    val locationNameState = _locationNameState.asStateFlow()
        .filterNotNull()

    private val _episodesListState = MutableStateFlow<List<EpisodeEntity>?>(null)
    val episodesListState = _episodesListState.asStateFlow()
        .filterNotNull()

    private val _errorState = MutableStateFlow<Any?>(null)
    val errorState = _errorState.asStateFlow()
        .filterNotNull()

    private var characterJob: Job? = null
    private var locationNameJob: Job? = null
    private var originNameJob: Job? = null
    private var episodesJob: Job? = null

    fun onViewCreated(characterId: Int) {
        provideCharacterFlow(characterId)
    }

    fun onButtonReloadPressed(characterId: Int) {
        characterJob?.cancel()
        locationNameJob?.cancel()
        originNameJob?.cancel()
        episodesJob?.cancel()
        provideCharacterFlow(characterId)
    }

    private fun provideCharacterFlow(characterId: Int) {
        characterJob = viewModelScope.launch(Dispatchers.IO) {
            getSingleCharacterUseCase.invoke(characterId)
                .catch {
                    emitError()
                }
                .collect {
                    _characterState.tryEmit(it)
                    provideExtraCharacterDetails(it)
                }
        }
    }

    private fun provideExtraCharacterDetails(entity: CharacterEntity) {
        loadLocationAndOrigin(entity)
        loadEpisodes(entity.episodesId)
        provideLocationNameFlow(entity.locationId)
        provideOriginNameFlow(entity.originId)
        provideEpisodesFlow(entity.episodesId)
    }

    private fun loadLocationAndOrigin(entity: CharacterEntity) {
        val ids = mutableListOf<Int>()
        if (entity.locationId != UNDEFINED_ID) {
            ids.add(entity.locationId)
        }
        if (entity.originId != UNDEFINED_ID) {
            ids.add(entity.originId)
        }
        viewModelScope.launch(Dispatchers.IO) {
            loadLocationsByIdUseCase.invoke(ids.toList())
        }
    }

    private fun loadEpisodes(ids: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            loadEpisodesByIdUseCase.invoke(ids)
        }
    }

    private fun provideLocationNameFlow(locationId: Int) {
        if (locationId == UNDEFINED_ID) {
            _locationNameState.tryEmit(UNKNOWN_VALUE)
            return
        }
        locationNameJob = viewModelScope.launch(Dispatchers.IO) {
            getSingleLocationUseCase.invoke(locationId)
                .catch {
                    emitError()
                }
                .collect {
                    _locationNameState.tryEmit(it.name)
                }
        }
    }

    private fun provideOriginNameFlow(originId: Int) {
        if (originId == UNDEFINED_ID) {
            _originNameState.tryEmit(UNKNOWN_VALUE)
            return
        }
        originNameJob = viewModelScope.launch(Dispatchers.IO) {
            getSingleLocationUseCase.invoke(originId)
                .catch {
                    emitError()
                }
                .collect {
                    _originNameState.tryEmit(it.name)
                }
        }
    }

    private fun provideEpisodesFlow(ids: List<Int>) {
        if (ids.isEmpty()) {
            _episodesListState.tryEmit(emptyList())
            return
        }
        episodesJob = viewModelScope.launch(Dispatchers.IO) {
            getEpisodesByIdUseCase.invoke(ids)
                .catch {
                    emitError()
                }
                .collect {
                    _episodesListState.tryEmit(it)
                }
        }
    }

    private fun emitError() {
        _errorState.tryEmit(Any())
    }

    companion object {

        private const val UNDEFINED_ID = -1
        private const val UNKNOWN_VALUE = "Unknown"
    }
}

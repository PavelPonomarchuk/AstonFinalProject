package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetEpisodesByIdUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetLocationNameUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleCharacterUseCase
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val getSingleCharacterUseCase: GetSingleCharacterUseCase,
    private val getEpisodesByIdUseCase: GetEpisodesByIdUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase
) : ViewModel() {

    private var _characterState = MutableStateFlow<CharacterEntity?>(null)
    val characterState = _characterState.asStateFlow()
        .filterNotNull()

    private var _originNameState = MutableStateFlow<String?>(null)
    val originNameState = _originNameState.asStateFlow()
        .filterNotNull()

    private var _locationNameState = MutableStateFlow<String?>(null)
    val locationNameState = _locationNameState.asStateFlow()
        .filterNotNull()

    private var _episodesListState = MutableStateFlow<List<EpisodeEntity>?>(null)
    val episodesListState = _episodesListState.asStateFlow()
        .filterNotNull()

    private var _errorState = MutableStateFlow<Boolean?>(null)
    val errorState = _errorState.asStateFlow()
        .filterNotNull()

    private var entity: CharacterEntity? = null
    private var originName: String = UNDEFINED_NAME
    private var locationName: String = UNDEFINED_NAME
    private val episodesList = mutableListOf<EpisodeEntity>()

    fun onViewCreated(characterId: Int) {
        loadCharacter(characterId)
    }

    private fun loadCharacter(characterId: Int) {
        viewModelScope.launch {
            val characterResult = getSingleCharacterUseCase.invoke(characterId)
            if (characterResult != null) {
                entity = characterResult
                loadOriginName()
            } else {
                emitError()
            }
        }
    }

    private fun loadOriginName() {
        entity?.originId?.let {
            loadName(it) { name ->
                originName = name
                loadLocationName()
            }
        }
    }

    private fun loadLocationName() {
        entity?.locationId?.let {
            loadName(it) { name ->
                locationName = name
                loadEpisodes()
            }
        }
    }

    private fun loadName(id: Int, callback: (name: String) -> Unit) {
        viewModelScope.launch {
            val result = getLocationNameUseCase.invoke(id)
            result?.let {
                callback.invoke(it)
            }
            if (result == null) {
                emitError()
            }
        }
    }

    private fun loadEpisodes() {
        viewModelScope.launch {
            val episodesResult = entity?.episodesId?.let { getEpisodesByIdUseCase.invoke(it) }
            episodesResult?.let {
                episodesList.addAll(it)
                emitData()
            }
            if (episodesResult == null) {
                emitError()
            }
        }
    }

    private fun emitData() {
        _characterState.tryEmit(entity)
        _episodesListState.tryEmit(episodesList)
        _originNameState.tryEmit(originName)
        _locationNameState.tryEmit(locationName)
    }

    private fun emitError() {
        _errorState.tryEmit(true)
    }

    companion object {

        private const val UNDEFINED_NAME = ""
    }
}

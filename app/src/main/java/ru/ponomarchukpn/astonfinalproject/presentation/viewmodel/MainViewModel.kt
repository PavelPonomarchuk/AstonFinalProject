package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetEpisodesPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetLocationsPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleCharacterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleEpisodeUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleLocationUseCase
import ru.ponomarchukpn.astonfinalproject.presentation.screens.EventWrapper
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCharactersPageUseCase: GetCharactersPageUseCase,
    private val getSingleCharacterUseCase: GetSingleCharacterUseCase,
    private val getLocationsPageUseCase: GetLocationsPageUseCase,
    private val getSingleLocationUseCase: GetSingleLocationUseCase,
    private val getEpisodesPageUseCase: GetEpisodesPageUseCase,
    private val getSingleEpisodeUseCase: GetSingleEpisodeUseCase
) : ViewModel() {

    private val _charactersLiveData = MutableLiveData<List<CharacterEntity>>()
    val charactersLiveData: LiveData<List<CharacterEntity>>
        get() = _charactersLiveData

    private val _singleCharacterLiveData = MutableLiveData<EventWrapper<CharacterEntity>>()
    val singleCharacterLiveData: LiveData<EventWrapper<CharacterEntity>>
        get() = _singleCharacterLiveData

    private val charactersList = mutableListOf<CharacterEntity>()

//    fun loadCharactersPage() {
//        viewModelScope.launch {
//            getCharactersPageUseCase.invoke()
//                .flowOn(Dispatchers.IO)
//                .catch {
//
//                }
//                .collect {
//                    processCharactersPageResponse(it)
//                }
//        }
//    }
//
//    fun loadCharacter(characterId: Int) {
//        viewModelScope.launch {
//            getSingleCharacterUseCase.invoke(characterId)
//                .flowOn(Dispatchers.IO)
//                .catch {
//
//                }
//                .collect {
//                    processCharacterResponse(it)
//                }
//        }
//    }

    private fun processCharacterResponse(character: CharacterEntity) {
        _singleCharacterLiveData.value = EventWrapper(character)
    }

    private fun processCharactersPageResponse(characters: List<CharacterEntity>) {
        if (characters.isNotEmpty()) {
            charactersList.addAll(characters)
            _charactersLiveData.value = charactersList.toList()
        }
    }

//    fun loadLocationsPage() {
//        viewModelScope.launch {
//            getLocationsPageUseCase.invoke()
//                .flowOn(Dispatchers.IO)
//                .catch {
//
//                }
//                .collect {
//                    processLocationsPageResponse(it)
//                }
//        }
//    }
//
//    fun loadLocation(locationId: Int) {
//        viewModelScope.launch {
//            getSingleLocationUseCase.invoke(locationId)
//                .flowOn(Dispatchers.IO)
//                .catch {
//
//                }
//                .collect {
//                    processLocationResponse(it)
//                }
//        }
//    }

    private fun processLocationResponse(location: LocationEntity) {
        Log.d("downloading_test", location.toString())
    }

    private fun processLocationsPageResponse(locations: List<LocationEntity>) {
        Log.d("downloading_test", locations.toString())
    }

//    fun loadEpisodesPage() {
//        viewModelScope.launch {
//            getEpisodesPageUseCase.invoke()
//                .flowOn(Dispatchers.IO)
//                .catch {
//
//                }
//                .collect {
//                    processEpisodesPageResponse(it)
//                }
//        }
//    }
//
//    fun loadEpisode(episodeId: Int) {
//        viewModelScope.launch {
//            getSingleEpisodeUseCase.invoke(episodeId)
//                .flowOn(Dispatchers.IO)
//                .catch {
//
//                }
//                .collect {
//                    processEpisodeResponse(it)
//                }
//        }
//    }

    private fun processEpisodeResponse(episode: EpisodeEntity) {
        Log.d("downloading_test", episode.toString())
    }

    private fun processEpisodesPageResponse(episodes: List<EpisodeEntity>) {
        Log.d("downloading_test", episodes.toString())
    }
}

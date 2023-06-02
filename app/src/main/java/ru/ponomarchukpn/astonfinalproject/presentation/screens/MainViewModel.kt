package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.common.MyApp
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetEpisodesPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetLocationsPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleCharacterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleEpisodeUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleLocationUseCase
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var getCharactersPageUseCase: GetCharactersPageUseCase

    @Inject
    lateinit var getSingleCharacterUseCase: GetSingleCharacterUseCase

    @Inject
    lateinit var getLocationsPageUseCase: GetLocationsPageUseCase

    @Inject
    lateinit var getSingleLocationUseCase: GetSingleLocationUseCase

    @Inject
    lateinit var getEpisodesPageUseCase: GetEpisodesPageUseCase

    @Inject
    lateinit var getSingleEpisodeUseCase: GetSingleEpisodeUseCase

    private val appComponent by lazy {
        MyApp.myApp.appComponent
    }

    private val _charactersLiveData = MutableLiveData<List<CharacterEntity>>()
    val charactersLiveData: LiveData<List<CharacterEntity>>
        get() = _charactersLiveData

    private val _singleCharacterLiveData = MutableLiveData<EventWrapper<CharacterEntity>>()
    val singleCharacterLiveData: LiveData<EventWrapper<CharacterEntity>>
        get() = _singleCharacterLiveData

    private val charactersList = mutableListOf<CharacterEntity>()

    init {
        appComponent.inject(this)
    }

    fun loadCharactersPage() {
        viewModelScope.launch {
            getCharactersPageUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .catch {
                    //TODO нужно для показа сообщения пользователю
                }
                .collect {
                    processCharactersPageResponse(it)
                }
        }
    }

    fun loadCharacter(characterId: Int) {
        viewModelScope.launch {
            getSingleCharacterUseCase.invoke(characterId)
                .flowOn(Dispatchers.IO)
                .catch {
                    //TODO нужно для показа сообщения пользователю
                }
                .collect {
                    processCharacterResponse(it)
                }
        }
    }

    private fun processCharacterResponse(character: CharacterEntity) {
        _singleCharacterLiveData.value = EventWrapper(character)
    }

    private fun processCharactersPageResponse(characters: List<CharacterEntity>) {
        if (characters.isNotEmpty()) {
            charactersList.addAll(characters)
            _charactersLiveData.value = charactersList.toList()
        }
    }

    fun loadLocationsPage() {
        viewModelScope.launch {
            getLocationsPageUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .catch {
                    //TODO нужно для показа сообщения пользователю
                }
                .collect {
                    processLocationsPageResponse(it)
                }
        }
    }

    fun loadLocation(locationId: Int) {
        viewModelScope.launch {
            getSingleLocationUseCase.invoke(locationId)
                .flowOn(Dispatchers.IO)
                .catch {
                    //TODO нужно для показа сообщения пользователю
                }
                .collect {
                    processLocationResponse(it)
                }
        }
    }

    private fun processLocationResponse(location: LocationEntity) {
        Log.d("downloading_test", location.toString())
    }

    private fun processLocationsPageResponse(locations: List<LocationEntity>) {
        Log.d("downloading_test", locations.toString())
    }

    fun loadEpisodesPage() {
        viewModelScope.launch {
            getEpisodesPageUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .catch {
                    //TODO нужно для показа сообщения пользователю
                }
                .collect {
                    processEpisodesPageResponse(it)
                }
        }
    }

    fun loadEpisode(episodeId: Int) {
        viewModelScope.launch {
            getSingleEpisodeUseCase.invoke(episodeId)
                .flowOn(Dispatchers.IO)
                .catch {
                    //TODO нужно для показа сообщения пользователю
                }
                .collect {
                    processEpisodeResponse(it)
                }
        }
    }

    private fun processEpisodeResponse(episode: EpisodeEntity) {
        Log.d("downloading_test", episode.toString())
    }

    private fun processEpisodesPageResponse(episodes: List<EpisodeEntity>) {
        Log.d("downloading_test", episodes.toString())
    }
}

package ru.ponomarchukpn.astonfinalproject.presentation.screens

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
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleCharacterUseCase
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var getCharactersPageUseCase: GetCharactersPageUseCase

    @Inject
    lateinit var getSingleCharacterUseCase: GetSingleCharacterUseCase

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

    fun loadNextPage() {
        viewModelScope.launch {
            getCharactersPageUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .catch {
                    //TODO нужно для показа сообщения пользователю
                }
                .collect {
                    processPageResponse(it)
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

    private fun processPageResponse(characters: List<CharacterEntity>) {
        if (characters.isNotEmpty()) {
            charactersList.addAll(characters)
            _charactersLiveData.value = charactersList.toList()
        }
    }
}

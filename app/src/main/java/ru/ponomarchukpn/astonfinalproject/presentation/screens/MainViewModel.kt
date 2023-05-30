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
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var getCharactersPageUseCase: GetCharactersPageUseCase

    private val appComponent by lazy {
        MyApp.myApp.appComponent
    }

    private val _charactersLiveData = MutableLiveData<List<CharacterEntity>>()
    val charactersLiveData: LiveData<List<CharacterEntity>>
        get() = _charactersLiveData

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
                    processResponse(it)
                }
        }
    }

    private fun processResponse(characters: List<CharacterEntity>) {
        if (characters.isNotEmpty()) {
            charactersList.addAll(characters)
            _charactersLiveData.value = charactersList.toList()
        }
    }
}

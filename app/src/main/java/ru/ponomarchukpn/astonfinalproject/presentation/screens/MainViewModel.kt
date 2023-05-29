package ru.ponomarchukpn.astonfinalproject.presentation.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersPageResponse
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersPageUseCase

class MainViewModel : ViewModel() {
    //TODO инжектить юзкейсы, и м.б. фабрика для вью модели нужна

    private val _charactersLiveData = MutableLiveData<List<CharacterEntity>>()
    val charactersLiveData: LiveData<List<CharacterEntity>>
        get() = _charactersLiveData

    private val charactersList = mutableListOf<CharacterEntity>()

    private var pageNumber = INITIAL_PAGE_NUMBER
    private var hasNextPage = HAS_NEXT_PAGE_DEFAULT

    fun loadNextPage() {
        if (pageNumber > INITIAL_PAGE_NUMBER && !hasNextPage) {
            return
        }
        pageNumber++
        viewModelScope.launch {
            GetCharactersPageUseCase().invoke(pageNumber)
                .flowOn(Dispatchers.IO)
                .catch {
                    //TODO нужно для показа сообщения пользователю
                }
                .collect {
                    processResponse(it)
                }
        }
    }

    private fun processResponse(response: CharactersPageResponse) {
        hasNextPage = response.hasNextPage
        charactersList.addAll(response.characters)
        _charactersLiveData.value = charactersList.toList()
    }

    companion object {

        private const val INITIAL_PAGE_NUMBER = 0
        private const val HAS_NEXT_PAGE_DEFAULT = true
    }
}

package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.ResetCharactersPageUseCase
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val getCharactersPageUseCase: GetCharactersPageUseCase,
    private val resetCharactersPageUseCase: ResetCharactersPageUseCase
) : ViewModel() {

    private var _charactersListState = MutableStateFlow<List<CharacterEntity>?>(null)
    val charactersListState = _charactersListState.asStateFlow()
        .filterNotNull()

    private val charactersList = mutableListOf<CharacterEntity>()
    private var searchQuery = EMPTY_QUERY

    fun onViewCreated() {
        nextPage()
    }

    fun onListEnded() {
        nextPage()
    }

    fun onListSwiped() {
        resetCharactersPageUseCase.invoke()
        nextPage()
    }

    fun onSearchQueryChanged(query: String?) {
        searchQuery = query ?: EMPTY_QUERY
        emitFilteredWithQuery()
    }

    private fun emitFilteredWithQuery() {
        if (searchQuery != EMPTY_QUERY) {
            charactersList.filter {
                it.name.contains(searchQuery, true)
            }.also {
                _charactersListState.tryEmit(it)
            }
        } else {
            _charactersListState.tryEmit(charactersList.toList())
        }
    }

    //TODO косяк в логике получения данных
    //при переключении табов и возврате назад показывается список со следующей страницы
    //то же при повороте экрана, загружается следующая страница
    private fun nextPage() {
        viewModelScope.launch {
            val page = getCharactersPageUseCase.invoke()
            if (page.isNotEmpty()) {
                charactersList.addAll(page)
                emitFilteredWithQuery()
            }
        }
    }

    companion object {

        private const val EMPTY_QUERY = ""
    }
}

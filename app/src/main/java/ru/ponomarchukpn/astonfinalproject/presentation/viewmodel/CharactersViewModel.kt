package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.LoadCharactersPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.SaveCharactersFilterUseCase
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val saveCharactersFilterUseCase: SaveCharactersFilterUseCase,
    private val getCharactersFilterUseCase: GetCharactersFilterUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val loadCharactersPageUseCase: LoadCharactersPageUseCase,
    private val pageHolder: CharactersPageHolder,
    private val matcher: CharactersMatcher
) : ViewModel() {

    private val _charactersListState = MutableSharedFlow<List<CharacterEntity>>(1)
    val charactersListState = _charactersListState.asSharedFlow()

    private val _notEmptyFilterState = MutableStateFlow<Boolean?>(null)
    val notEmptyFilterState = _notEmptyFilterState.asStateFlow()
        .filterNotNull()

    private val _errorState = MutableStateFlow<Any?>(null)
    val errorState = _errorState.asStateFlow()
        .filterNotNull()

    private val _emptyResultState = MutableStateFlow<Any?>(null)
    val emptyResultState = _emptyResultState.asStateFlow()
        .filterNotNull()

    private var job: Job? = null
    private val emptyFilterSettings = CharactersFilterSettings(
        EMPTY_STRING, null, EMPTY_STRING, EMPTY_STRING, null
    )
    private var searchQuery = EMPTY_STRING

    fun onViewCreated() {
        if (pageHolder.currentPageNumber() == INITIAL_PAGE_NUMBER) {
            loadPage()
        }
        provideCharactersFlow()
        loadFilterIsPresent()
    }

    fun onListEnded() {
        loadPage()
    }

    fun onListSwiped() {
        resetData()
    }

    fun onFilterSettingsChanged() {
        resetData()
    }

    fun onButtonClearPressed() {
        viewModelScope.launch(Dispatchers.IO) {
            val emptySettingsSaved = saveCharactersFilterUseCase.invoke(emptyFilterSettings)
            if (emptySettingsSaved) {
                resetData()
                _notEmptyFilterState.tryEmit(false)
            }
        }
    }

    fun onSearchQueryChanged(query: String?) {
        searchQuery = query?.trim() ?: EMPTY_STRING
        resetData()
    }

    private fun provideCharactersFlow() {
        job = viewModelScope.launch(Dispatchers.IO) {
            getCharactersUseCase.invoke()
                .catch {
                    emitError()
                }
                .collect { charactersList ->
                    val filter = getCharactersFilterUseCase.invoke()
                    val filtered = charactersList.filter { matcher.isCharacterMatches(filter, it) }
                    emitFilteredWithQuery(filtered)
                }
        }
    }

    private fun emitFilteredWithQuery(characters: List<CharacterEntity>) {
        if (searchQuery != EMPTY_STRING) {
            characters.filter {
                it.name.contains(searchQuery, true)
            }.also {
                _charactersListState.tryEmit(it)
                if (it.isEmpty()) {
                    emitEmptyResultState()
                }
            }
        } else {
            _charactersListState.tryEmit(characters)
            if (characters.isEmpty()) {
                emitEmptyResultState()
            }
        }
    }

    private fun emitError() {
        _errorState.tryEmit(Any())
    }

    private fun emitEmptyResultState() {
        _emptyResultState.tryEmit(Any())
    }

    private fun resetData() {
        job?.cancel()
        provideCharactersFlow()
    }

    private fun loadPage() {
        viewModelScope.launch(Dispatchers.IO) {
            var pageNumber = pageHolder.currentPageNumber()
            val success = loadCharactersPageUseCase.invoke(pageNumber)
            if (success) {
                pageNumber++
                pageHolder.savePageNumber(pageNumber)
            } else {
                emitError()
            }
        }
    }

    private fun loadFilterIsPresent() {
        viewModelScope.launch(Dispatchers.IO) {
            val filterSettings = getCharactersFilterUseCase.invoke()
            if (filterSettings != emptyFilterSettings) {
                _notEmptyFilterState.tryEmit(true)
            } else {
                _notEmptyFilterState.tryEmit(false)
            }
        }
    }

    companion object {

        private const val EMPTY_STRING = ""
        private const val INITIAL_PAGE_NUMBER = 1
    }
}

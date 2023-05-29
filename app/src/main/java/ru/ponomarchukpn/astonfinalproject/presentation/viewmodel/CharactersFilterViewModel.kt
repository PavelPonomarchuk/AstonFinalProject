package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.SaveCharactersFilterUseCase
import javax.inject.Inject

class CharactersFilterViewModel @Inject constructor(
    private val getCharactersFilterUseCase: GetCharactersFilterUseCase,
    private val saveCharactersFilterUseCase: SaveCharactersFilterUseCase
) : ViewModel() {

    private val _charactersFilterState = MutableStateFlow<CharactersFilterSettings?>(null)
    val charactersFilterState = _charactersFilterState.asStateFlow()
        .filterNotNull()

    private val _filterSavedState = MutableStateFlow<Unit?>(null)
    val filterSavedState = _filterSavedState.asStateFlow()
        .filterNotNull()

    fun onViewCreated() {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = getCharactersFilterUseCase.invoke()
            _charactersFilterState.tryEmit(settings)
        }
    }

    fun onApplyPressed(settings: CharactersFilterSettings) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = saveCharactersFilterUseCase.invoke(settings)
            if (success) {
                _filterSavedState.tryEmit(Unit)
            }
        }
    }
}

package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersByIdUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleLocationUseCase
import javax.inject.Inject

class LocationDetailsViewModel @Inject constructor(
    private val getSingleLocationUseCase: GetSingleLocationUseCase,
    private val getCharactersByIdUseCase: GetCharactersByIdUseCase
) : ViewModel() {

    private var _locationState = MutableStateFlow<LocationEntity?>(null)
    val locationState = _locationState.asStateFlow()
        .filterNotNull()

    private var _charactersListState = MutableStateFlow<List<CharacterEntity>?>(null)
    val charactersListState = _charactersListState.asStateFlow()
        .filterNotNull()

    private var _errorState = MutableStateFlow<Boolean?>(null)
    val errorState = _errorState.asStateFlow()
        .filterNotNull()

    private var entity: LocationEntity? = null
    private val charactersList = mutableListOf<CharacterEntity>()

    fun onViewCreated(locationId: Int) {
        loadLocation(locationId)
    }

    private fun loadLocation(locationId: Int) {
        viewModelScope.launch {
            val result = getSingleLocationUseCase.invoke(locationId)
            result?.let {
                entity = result
                loadCharacters()
            } ?: emitError()
        }
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            val result = entity?.residentsId?.let { getCharactersByIdUseCase.invoke(it) }
            result?.let {
                charactersList.addAll(it)
                emitData()
            } ?: emitError()
        }
    }

    private fun emitData() {
        _locationState.tryEmit(entity)
        _charactersListState.tryEmit(charactersList)
    }

    private fun emitError() {
        _errorState.tryEmit(true)
    }
}

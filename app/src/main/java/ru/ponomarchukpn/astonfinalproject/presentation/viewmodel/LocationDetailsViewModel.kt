package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersByIdUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleLocationUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.LoadCharactersByIdUseCase
import javax.inject.Inject

class LocationDetailsViewModel @Inject constructor(
    private val getSingleLocationUseCase: GetSingleLocationUseCase,
    private val getCharactersByIdUseCase: GetCharactersByIdUseCase,
    private val loadCharactersByIdUseCase: LoadCharactersByIdUseCase
) : ViewModel() {

    private val _locationState = MutableStateFlow<LocationEntity?>(null)
    val locationState = _locationState.asStateFlow()
        .filterNotNull()

    private val _charactersListState = MutableStateFlow<List<CharacterEntity>?>(null)
    val charactersListState = _charactersListState.asStateFlow()
        .filterNotNull()

    private val _errorState = MutableStateFlow<Any?>(null)
    val errorState = _errorState.asStateFlow()
        .filterNotNull()

    private var locationJob: Job? = null
    private var charactersJob: Job? = null

    fun onViewCreated(locationId: Int) {
        provideLocationFlow(locationId)
    }

    fun onButtonReloadPressed(locationId: Int) {
        locationJob?.cancel()
        charactersJob?.cancel()
        provideLocationFlow(locationId)
    }

    private fun provideLocationFlow(locationId: Int) {
        locationJob = viewModelScope.launch(Dispatchers.IO) {
            getSingleLocationUseCase.invoke(locationId)
                .catch {
                    emitError()
                }
                .collect {
                    _locationState.tryEmit(it)
                    provideExtraLocationDetails(it)
                }
        }
    }

    private fun provideExtraLocationDetails(entity: LocationEntity) {
        loadCharacters(entity.residentsId)
        provideCharactersFlow(entity.residentsId)
    }

    private fun loadCharacters(ids: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            loadCharactersByIdUseCase.invoke(ids)
        }
    }

    private fun provideCharactersFlow(ids: List<Int>) {
        if (ids.isEmpty()) {
            _charactersListState.tryEmit(emptyList())
            return
        }
        charactersJob = viewModelScope.launch(Dispatchers.IO) {
            getCharactersByIdUseCase.invoke(ids)
                .catch {
                    emitError()
                }
                .collect {
                    _charactersListState.tryEmit(it)
                }
        }
    }

    private fun emitError() {
        _errorState.tryEmit(Any())
    }
}

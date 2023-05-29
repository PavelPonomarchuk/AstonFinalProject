package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationsFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetLocationsFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.SaveLocationsFilterUseCase
import javax.inject.Inject

class LocationsFilterViewModel @Inject constructor(
    private val getLocationsFilterUseCase: GetLocationsFilterUseCase,
    private val saveLocationsFilterUseCase: SaveLocationsFilterUseCase
) : ViewModel() {

    private val _locationsFilterState = MutableStateFlow<LocationsFilterSettings?>(null)
    val locationsFilterState = _locationsFilterState.asStateFlow()
        .filterNotNull()

    private val _filterSavedState = MutableStateFlow<Unit?>(null)
    val filterSavedState = _filterSavedState.asStateFlow()
        .filterNotNull()

    fun onViewCreated() {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = getLocationsFilterUseCase.invoke()
            _locationsFilterState.tryEmit(settings)
        }
    }

    fun onApplyPressed(settings: LocationsFilterSettings) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = saveLocationsFilterUseCase.invoke(settings)
            if (success) {
                _filterSavedState.tryEmit(Unit)
            }
        }
    }
}

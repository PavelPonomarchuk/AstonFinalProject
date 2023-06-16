package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodesFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetEpisodesFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.SaveEpisodesFilterUseCase
import javax.inject.Inject

class EpisodesFilterViewModel @Inject constructor(
    private val getEpisodesFilterUseCase: GetEpisodesFilterUseCase,
    private val saveEpisodesFilterUseCase: SaveEpisodesFilterUseCase
) : ViewModel() {

    private var _episodesFilterState = MutableStateFlow<EpisodesFilterSettings?>(null)
    val episodesFilterState = _episodesFilterState.asStateFlow()
        .filterNotNull()

    private var _filterSavedState = MutableStateFlow<Boolean?>(null)
    val filterSavedState = _filterSavedState.asStateFlow()
        .filterNotNull()

    fun onViewCreated() {
        viewModelScope.launch {
            val settings = getEpisodesFilterUseCase.invoke()
            _episodesFilterState.tryEmit(settings)
        }
    }

    fun onApplyPressed(settings: EpisodesFilterSettings) {
        viewModelScope.launch {
            val success = saveEpisodesFilterUseCase.invoke(settings)
            if (success) {
                _filterSavedState.tryEmit(true)
            }
        }
    }
}

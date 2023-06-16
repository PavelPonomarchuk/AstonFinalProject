package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationsFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository

class SaveLocationsFilterUseCase(
    private val repository: LocationsRepository
) {

    suspend operator fun invoke(settings: LocationsFilterSettings) =
        repository.saveFilterSettings(settings)
}

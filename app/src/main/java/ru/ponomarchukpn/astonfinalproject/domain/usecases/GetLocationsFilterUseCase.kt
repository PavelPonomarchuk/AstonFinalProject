package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository
import javax.inject.Inject

class GetLocationsFilterUseCase @Inject constructor(
    private val repository: LocationsRepository
) {

    suspend operator fun invoke() = repository.getFilterSettings()
}

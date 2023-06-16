package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository

class GetLocationNameUseCase(
    private val repository: LocationsRepository
) {

    suspend operator fun invoke(locationId: Int) = repository.getLocation(locationId)?.name
}

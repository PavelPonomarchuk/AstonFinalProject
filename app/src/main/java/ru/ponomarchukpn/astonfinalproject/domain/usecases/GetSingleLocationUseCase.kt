package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository

class GetSingleLocationUseCase(
    private val repository: LocationsRepository
) {

    suspend operator fun invoke(locationId: Int) = repository.getLocation(locationId)
}

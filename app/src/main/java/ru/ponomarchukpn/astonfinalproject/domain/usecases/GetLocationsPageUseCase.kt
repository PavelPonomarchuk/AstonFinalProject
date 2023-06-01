package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository

class GetLocationsPageUseCase(
    private val repository: LocationsRepository
) {

    operator fun invoke() = repository.getNextLocationsPage()
}

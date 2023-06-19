package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository
import javax.inject.Inject

class GetLocationNameUseCase @Inject constructor(
    private val repository: LocationsRepository
) {

    suspend operator fun invoke(locationId: Int) = repository.getLocation(locationId)?.name
}

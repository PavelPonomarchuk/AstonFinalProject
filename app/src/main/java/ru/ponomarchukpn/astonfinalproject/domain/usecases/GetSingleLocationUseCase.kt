package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository
import javax.inject.Inject

class GetSingleLocationUseCase @Inject constructor(
    private val repository: LocationsRepository
) {

    suspend operator fun invoke(locationId: Int) = repository.getLocation(locationId)
}

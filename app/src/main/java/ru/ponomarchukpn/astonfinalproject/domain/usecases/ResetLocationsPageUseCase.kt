package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository
import javax.inject.Inject

class ResetLocationsPageUseCase @Inject constructor(
    private val repository: LocationsRepository
) {

    operator fun invoke() = repository.resetPage()
}

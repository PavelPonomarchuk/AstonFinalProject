package ru.ponomarchukpn.astonfinalproject.domain.repository

import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationsFilterSettings

interface LocationsRepository {

    suspend fun getNextLocationsPage(): List<LocationEntity>

    suspend fun getLocation(locationId: Int): LocationEntity?

    suspend fun getFilterSettings(): LocationsFilterSettings

    suspend fun saveFilterSettings(settings: LocationsFilterSettings): Boolean

    fun resetPage()
}

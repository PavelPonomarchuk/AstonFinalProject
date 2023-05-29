package ru.ponomarchukpn.astonfinalproject.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationsFilterSettings

interface LocationsRepository {

    fun getLocations(): Flow<List<LocationEntity>>

    suspend fun loadLocationsPage(pageNumber: Int): Boolean

    fun getLocation(locationId: Int): Flow<LocationEntity>

    suspend fun loadLocationsById(ids: List<Int>): Boolean

    suspend fun getFilterSettings(): LocationsFilterSettings

    suspend fun saveFilterSettings(settings: LocationsFilterSettings): Boolean
}

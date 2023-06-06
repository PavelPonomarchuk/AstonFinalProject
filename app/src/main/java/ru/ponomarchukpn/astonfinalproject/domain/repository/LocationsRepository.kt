package ru.ponomarchukpn.astonfinalproject.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity

interface LocationsRepository {

    suspend fun getNextLocationsPage(): Flow<List<LocationEntity>>

    suspend fun getLocation(locationId: Int): Flow<LocationEntity>
}

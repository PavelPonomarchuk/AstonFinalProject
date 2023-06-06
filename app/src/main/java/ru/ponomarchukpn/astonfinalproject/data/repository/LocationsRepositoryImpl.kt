package ru.ponomarchukpn.astonfinalproject.data.repository

import android.content.Context
import kotlinx.coroutines.flow.flow
import ru.ponomarchukpn.astonfinalproject.common.isInternetAvailable
import ru.ponomarchukpn.astonfinalproject.data.database.LocationsDao
import ru.ponomarchukpn.astonfinalproject.data.mapper.LocationMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.LocationsApiService
import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository
import javax.inject.Inject

class LocationsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val locationsDao: LocationsDao,
    private val apiService: LocationsApiService,
    private val mapper: LocationMapper
) : LocationsRepository {

    private var pageNumber = INITIAL_PAGE_NUMBER

    override suspend fun getNextLocationsPage() = flow {
        if (context.isInternetAvailable()) {
            try {
                val pageDto = apiService.loadPage(pageNumber)
                locationsDao.insertList(
                    mapper.mapPageToDbModelList(pageDto, pageNumber)
                )
                pageNumber++
                emit(mapper.mapPageToLocationsList(pageDto))
            } catch (exception: Throwable) {
                emit(emptyList())
            }
        } else {
            val dbModels = locationsDao.getPage(pageNumber)
            if (dbModels.isNotEmpty()) {
                pageNumber++
                emit(mapper.mapDbModelListToEntityList(dbModels))
            } else {
                emit(emptyList())
            }
        }
    }

    override suspend fun getLocation(locationId: Int) = flow {
        if (context.isInternetAvailable()) {
            val locationDto = apiService.loadItem(locationId)
            emit(mapper.mapDtoToEntity(locationDto))
        } else {
            val dbModel = locationsDao.getItem(locationId)
            emit(mapper.mapDbModelToEntity(dbModel))
        }
    }

    companion object {

        private const val INITIAL_PAGE_NUMBER = 1
    }
}

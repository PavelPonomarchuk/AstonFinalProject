package ru.ponomarchukpn.astonfinalproject.data.repository

import android.content.Context
import ru.ponomarchukpn.astonfinalproject.common.isInternetAvailable
import ru.ponomarchukpn.astonfinalproject.data.database.LocationsDao
import ru.ponomarchukpn.astonfinalproject.data.mapper.LocationMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.LocationsApiService
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationsFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository
import javax.inject.Inject

class LocationsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val locationsDao: LocationsDao,
    private val apiService: LocationsApiService,
    private val mapper: LocationMapper
) : LocationsRepository {

    private var pageNumber = INITIAL_PAGE_NUMBER
    private var filterSettings: LocationsFilterSettings? = null
    //TODO сделать постоянное хранение настроек фильтра

//    override suspend fun getNextLocationsPage() = flow {
//        if (context.isInternetAvailable()) {
//            try {
//                val pageDto = apiService.loadPage(pageNumber)
//                locationsDao.insertList(
//                    mapper.mapPageToDbModelList(pageDto, pageNumber)
//                )
//                pageNumber++
//                emit(mapper.mapPageToLocationsList(pageDto))
//            } catch (exception: Throwable) {
//                emit(emptyList())
//            }
//        } else {
//            val dbModels = locationsDao.getPage(pageNumber)
//            if (dbModels.isNotEmpty()) {
//                pageNumber++
//                emit(mapper.mapDbModelListToEntityList(dbModels))
//            } else {
//                emit(emptyList())
//            }
//        }
//    }

    override suspend fun getNextLocationsPage(): List<LocationEntity> {
        if (context.isInternetAvailable()) {
            return try {
                val pageDto = apiService.loadPage(pageNumber)
                locationsDao.insertList(
                    mapper.mapPageToDbModelList(pageDto, pageNumber)
                )
                pageNumber++
                mapper.mapPageToLocationsList(pageDto)
            } catch (exception: Throwable) {
                emptyList()
            }
        } else {
            val dbModels = locationsDao.getPage(pageNumber)
            return if (dbModels.isNotEmpty()) {
                pageNumber++
                mapper.mapDbModelListToEntityList(dbModels)
            } else {
                emptyList()
            }
        }
    }

//    override suspend fun getLocation(locationId: Int) = flow {
//        if (context.isInternetAvailable()) {
//            val locationDto = apiService.loadItem(locationId)
//            emit(mapper.mapDtoToEntity(locationDto))
//        } else {
//            val dbModel = locationsDao.getItem(locationId)
//            emit(mapper.mapDbModelToEntity(dbModel))
//        }
//    }

    //TODO возвращать налл если не удалось получить локацию по ид
    override suspend fun getLocation(locationId: Int) = if (context.isInternetAvailable()) {
        val locationDto = apiService.loadItem(locationId)
        mapper.mapDtoToEntity(locationDto)
    } else {
        val dbModel = locationsDao.getItem(locationId)
        mapper.mapDbModelToEntity(dbModel)
    }

    override fun resetPage() {
        pageNumber = INITIAL_PAGE_NUMBER
        //TODO разрулить: сброс м.б. пока данные загружаются и прежнее значение ещё используется
    }

    override suspend fun getFilterSettings() = filterSettings ?: LocationsFilterSettings(
        EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE
    )

    override suspend fun saveFilterSettings(settings: LocationsFilterSettings): Boolean {
        filterSettings = settings
        return true
    }

    companion object {

        private const val INITIAL_PAGE_NUMBER = 1
        private const val EMPTY_VALUE = ""
    }
}

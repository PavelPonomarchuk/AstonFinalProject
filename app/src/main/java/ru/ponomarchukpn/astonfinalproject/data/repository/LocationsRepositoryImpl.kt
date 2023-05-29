package ru.ponomarchukpn.astonfinalproject.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import ru.ponomarchukpn.astonfinalproject.data.database.LocationsDao
import ru.ponomarchukpn.astonfinalproject.data.mapper.LocationMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.LocationsApiService
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationsFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository
import javax.inject.Inject

class LocationsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val locationsDao: LocationsDao,
    private val apiService: LocationsApiService,
    private val mapper: LocationMapper
) : LocationsRepository {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        LOCATIONS_PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    override suspend fun getFilterSettings(): LocationsFilterSettings {
        val json = preferences.getString(KEY_LOCATIONS_FILTER, null)
        return json?.let {
            Gson().fromJson(json, LocationsFilterSettings::class.java)
        } ?: LocationsFilterSettings(EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE)
    }

    override suspend fun saveFilterSettings(settings: LocationsFilterSettings): Boolean {
        val json = Gson().toJson(settings)
        preferences.edit().putString(KEY_LOCATIONS_FILTER, json).apply()
        return true
    }

    override fun getLocations() = locationsDao.getAll().map {
        mapper.mapDbModelListToEntityList(it)
    }

    override suspend fun loadLocationsPage(pageNumber: Int): Boolean {
        return try {
            val pageDto = apiService.loadPage(pageNumber)
            locationsDao.insertList(
                mapper.mapPageToDbModelList(pageDto)
            )
            true
        } catch (exception: Throwable) {
            false
        }
    }

    override fun getLocation(locationId: Int) = locationsDao.getItem(locationId).map {
        mapper.mapDbModelToEntity(it)
    }

    override suspend fun loadLocationsById(ids: List<Int>): Boolean {
        if (ids.isEmpty()) {
            return true
        }
        return try {
            val locations = apiService.loadItemsByIds(ids.joinToString(","))
            locationsDao.insertList(
                mapper.mapDtoListToDbModelList(locations)
            )
            true
        } catch (exception: Throwable) {
            false
        }
    }

    companion object {

        private const val LOCATIONS_PREFERENCES_NAME = "locationsRepositoryPreferences"
        private const val KEY_LOCATIONS_FILTER = "locationsFilter"
        private const val EMPTY_VALUE = ""
    }
}

package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationsDao {

    @Query("SELECT * FROM locations WHERE relatedToPage == :page ORDER BY id ASC")
    suspend fun getLocationsPage(page: Int): List<LocationDbModel>

    @Query("SELECT * FROM locations WHERE id == :locationId LIMIT 1")
    suspend fun getLocation(locationId: Int): LocationDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationsList(locationsList: List<LocationDbModel>)
}

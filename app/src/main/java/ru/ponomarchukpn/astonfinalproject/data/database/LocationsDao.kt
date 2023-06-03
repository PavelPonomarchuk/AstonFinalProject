package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LocationsDao : BaseDao<LocationDbModel> {

    @Query("SELECT * FROM $TABLE_NAME WHERE relatedToPage == :page ORDER BY id ASC")
    suspend fun getPage(page: Int): List<LocationDbModel>

    @Query("SELECT * FROM $TABLE_NAME WHERE id == :itemId LIMIT 1")
    suspend fun getItem(itemId: Int): LocationDbModel

    companion object {

        const val TABLE_NAME = "locations"
    }
}

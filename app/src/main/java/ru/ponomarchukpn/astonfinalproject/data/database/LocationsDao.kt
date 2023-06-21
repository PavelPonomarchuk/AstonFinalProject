package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationsDao : BaseDao<LocationDbModel> {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id")
    fun getAll(): Flow<List<LocationDbModel>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id == :itemId LIMIT 1")
    fun getItem(itemId: Int): Flow<LocationDbModel>

    @Query("SELECT * FROM $TABLE_NAME WHERE id IN (:ids)")
    fun getItemsByIds(ids: List<Int>): Flow<List<LocationDbModel>>

    companion object {

        const val TABLE_NAME = "locations"
    }
}

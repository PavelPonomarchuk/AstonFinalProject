package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodesDao : BaseDao<EpisodeDbModel> {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id")
    fun getAll(): Flow<List<EpisodeDbModel>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id == :itemId LIMIT 1")
    fun getItem(itemId: Int): Flow<EpisodeDbModel>

    @Query("SELECT * FROM $TABLE_NAME WHERE id IN (:ids)")
    fun getItemsByIds(ids: List<Int>): Flow<List<EpisodeDbModel>>

    companion object {

        const val TABLE_NAME = "episodes"
    }
}

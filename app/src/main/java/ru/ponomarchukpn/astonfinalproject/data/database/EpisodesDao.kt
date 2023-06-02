package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface EpisodesDao : BaseDao<EpisodeDbModel> {

    @Query("SELECT * FROM $TABLE_NAME WHERE relatedToPage == :page ORDER BY id ASC")
    suspend fun getPage(page: Int): List<EpisodeDbModel>

    @Query("SELECT * FROM $TABLE_NAME WHERE id == :itemId LIMIT 1")
    suspend fun getItem(itemId: Int): EpisodeDbModel

    companion object {

        const val TABLE_NAME = "episodes"
    }
}

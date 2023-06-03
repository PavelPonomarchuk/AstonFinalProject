package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CharactersDao : BaseDao<CharacterDbModel> {

    @Query("SELECT * FROM $TABLE_NAME WHERE relatedToPage == :page ORDER BY id ASC")
    suspend fun getPage(page: Int): List<CharacterDbModel>

    @Query("SELECT * FROM $TABLE_NAME WHERE id == :itemId LIMIT 1")
    suspend fun getItem(itemId: Int): CharacterDbModel

    companion object {

        const val TABLE_NAME = "characters"
    }
}

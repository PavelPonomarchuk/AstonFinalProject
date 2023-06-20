package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharactersDao : BaseDao<CharacterDbModel> {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id")
    fun getAll(): Flow<List<CharacterDbModel>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id == :itemId LIMIT 1")
    fun getItem(itemId: Int): Flow<CharacterDbModel>

    @Query("SELECT * FROM $TABLE_NAME WHERE id IN (:ids)")
    fun getItemsByIds(ids: List<Int>): Flow<List<CharacterDbModel>>

    companion object {

        const val TABLE_NAME = "characters"
    }
}

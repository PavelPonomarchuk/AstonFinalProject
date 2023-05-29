package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharactersDao {

    @Query("SELECT * FROM characters WHERE relatedToPage == :page ORDER BY id ASC")
    fun getCharactersPage(page: Int): List<CharacterDbModel>

    @Query("SELECT * FROM characters WHERE id == :characterId LIMIT 1")
    suspend fun getCharacter(characterId: Int): CharacterDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharactersList(charactersList: List<CharacterDbModel>)
}

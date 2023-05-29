package ru.ponomarchukpn.astonfinalproject.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersFilterSettings

interface CharactersRepository {

    fun getCharacters(): Flow<List<CharacterEntity>>

    suspend fun loadCharactersPage(pageNumber: Int): Boolean

    fun getCharacter(characterId: Int): Flow<CharacterEntity>

    fun getCharactersById(ids: List<Int>): Flow<List<CharacterEntity>>

    suspend fun loadCharactersById(ids: List<Int>): Boolean

    suspend fun getFilterSettings(): CharactersFilterSettings

    suspend fun saveFilterSettings(settings: CharactersFilterSettings): Boolean
}

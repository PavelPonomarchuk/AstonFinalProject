package ru.ponomarchukpn.astonfinalproject.domain.repository

import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersFilterSettings

interface CharactersRepository {

    suspend fun getNextCharactersPage(): List<CharacterEntity>

    suspend fun getCharacter(characterId: Int): CharacterEntity?

    suspend fun getCharactersById(ids: List<Int>): List<CharacterEntity>?

    suspend fun getFilterSettings(): CharactersFilterSettings

    suspend fun saveFilterSettings(settings: CharactersFilterSettings): Boolean

    fun resetPage()
}

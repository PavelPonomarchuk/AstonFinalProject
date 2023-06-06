package ru.ponomarchukpn.astonfinalproject.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity

interface CharactersRepository {

    suspend fun getNextCharactersPage(): Flow<List<CharacterEntity>>

    suspend fun getCharacter(characterId: Int): Flow<CharacterEntity>
}

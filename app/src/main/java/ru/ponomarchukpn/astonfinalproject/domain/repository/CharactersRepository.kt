package ru.ponomarchukpn.astonfinalproject.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity

interface CharactersRepository {

    fun getNextCharactersPage(): Flow<List<CharacterEntity>>

    fun getCharacter(characterId: Int): Flow<CharacterEntity>
}

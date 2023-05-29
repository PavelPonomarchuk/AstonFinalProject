package ru.ponomarchukpn.astonfinalproject.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersPageResponse

interface CharactersRepository {

    fun getCharactersPage(pageNumber: Int): Flow<CharactersPageResponse>

    fun getCharacter(characterId: Int): Flow<CharacterEntity>
}

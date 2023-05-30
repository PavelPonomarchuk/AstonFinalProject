package ru.ponomarchukpn.astonfinalproject.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.ponomarchukpn.astonfinalproject.data.database.AppDatabase
import ru.ponomarchukpn.astonfinalproject.data.mapper.CharacterMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.CharactersApiService
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val apiService: CharactersApiService,
    private val mapper: CharacterMapper
) : CharactersRepository {

    override fun getCharactersPage(pageNumber: Int) = flow {
        if (isInternetAvailable()) {
            val dto = apiService.loadCharactersPage(pageNumber)
            //сохранять данные в БД
            emit(mapper.mapCharactersPageToResponseEntity(dto))
        } else {
            //таким же образом получать из БД и возвращать
        }
    }

    override fun getCharacter(characterId: Int): Flow<CharacterEntity> {
        TODO("Not yet implemented")
    }

    private fun isInternetAvailable(): Boolean {
        //TODO
        return true
    }
}

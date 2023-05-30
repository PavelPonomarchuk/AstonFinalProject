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

    private var pageNumber = INITIAL_PAGE_NUMBER
    private var hasNextPage = HAS_NEXT_PAGE_DEFAULT

    override fun getNextCharactersPage() = flow {
        if (!hasNextPage) {
            emit(emptyList())
        }
        if (isInternetAvailable()) {
            pageNumber++
            val pageDto = apiService.loadCharactersPage(pageNumber)
            //сохранять данные в БД
            val responseDto = mapper.mapCharactersPageToResponseDto(pageDto)
            hasNextPage = responseDto.hasNextPage
            emit(responseDto.characters)
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

    companion object {

        private const val INITIAL_PAGE_NUMBER = 0
        private const val HAS_NEXT_PAGE_DEFAULT = true
    }
}

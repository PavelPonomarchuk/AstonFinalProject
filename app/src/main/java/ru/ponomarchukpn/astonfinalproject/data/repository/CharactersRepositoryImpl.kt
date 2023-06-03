package ru.ponomarchukpn.astonfinalproject.data.repository

import android.content.Context
import kotlinx.coroutines.flow.flow
import ru.ponomarchukpn.astonfinalproject.common.isInternetAvailable
import ru.ponomarchukpn.astonfinalproject.data.database.CharactersDao
import ru.ponomarchukpn.astonfinalproject.data.mapper.CharacterMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.CharactersApiService
import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val context: Context,
    private val charactersDao: CharactersDao,
    private val apiService: CharactersApiService,
    private val mapper: CharacterMapper
) : CharactersRepository {

    private var pageNumber = INITIAL_PAGE_NUMBER

    override fun getNextCharactersPage() = flow {
        if (context.isInternetAvailable()) {
            try {
                val pageDto = apiService.loadPage(pageNumber)
                charactersDao.insertList(
                    mapper.mapPageDtoToDbModelList(pageDto, pageNumber)
                )
                pageNumber++
                emit(mapper.mapPageToEntitiesList(pageDto))
            } catch (exception: Throwable) {
                emit(emptyList())
            }
        } else {
            val dbModels = charactersDao.getPage(pageNumber)
            if (dbModels.isNotEmpty()) {
                pageNumber++
                emit(mapper.mapDbModelsListToEntitiesList(dbModels))
            } else {
                emit(emptyList())
            }
        }
    }

    override fun getCharacter(characterId: Int) = flow {
        if (context.isInternetAvailable()) {
            val characterDto = apiService.loadItem(characterId)
            emit(mapper.mapDtoToEntity(characterDto))
        } else {
            val dbModel = charactersDao.getItem(characterId)
            emit(mapper.mapDbModelToEntity(dbModel))
        }
    }

    companion object {

        private const val INITIAL_PAGE_NUMBER = 1
    }
}

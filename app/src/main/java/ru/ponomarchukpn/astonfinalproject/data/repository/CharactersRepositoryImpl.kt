package ru.ponomarchukpn.astonfinalproject.data.repository

import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.ponomarchukpn.astonfinalproject.data.database.AppDatabase
import ru.ponomarchukpn.astonfinalproject.data.mapper.CharacterMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.CharactersApiService
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val context: Context,
    private val database: AppDatabase,
    private val apiService: CharactersApiService,
    private val mapper: CharacterMapper
) : CharactersRepository {

    private var pageNumber = INITIAL_PAGE_NUMBER

    override fun getNextCharactersPage() = flow {
        if (isInternetAvailable()) {
            try {
                val pageDto = apiService.loadCharactersPage(pageNumber)
                database.charactersDao().insertCharactersList(
                    mapper.mapPageDtoToDbModelList(pageDto, pageNumber)
                )
                pageNumber++
                val responseDto = mapper.mapCharactersPageToResponseDto(pageDto)
                emit(responseDto.characters)
            } catch (exception: Throwable) {
                emit(emptyList())
            }
        } else {
            val dbModels = database.charactersDao().getCharactersPage(pageNumber)
            if (dbModels.isNotEmpty()) {
                pageNumber++
                emit(mapper.mapDbModelsListToEntitiesList(dbModels))
            } else {
                emit(emptyList())
            }
        }
    }

    override fun getCharacter(characterId: Int): Flow<CharacterEntity> {
        TODO("Not yet implemented")
    }

    private fun isInternetAvailable(): Boolean {
        //TODO земенить deprecated функционал

        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    companion object {

        private const val INITIAL_PAGE_NUMBER = 1
    }
}

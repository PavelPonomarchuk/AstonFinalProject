package ru.ponomarchukpn.astonfinalproject.data.repository

import android.content.Context
import ru.ponomarchukpn.astonfinalproject.common.isInternetAvailable
import ru.ponomarchukpn.astonfinalproject.data.database.CharactersDao
import ru.ponomarchukpn.astonfinalproject.data.mapper.CharacterMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.CharactersApiService
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val context: Context,
    private val charactersDao: CharactersDao,
    private val apiService: CharactersApiService,
    private val mapper: CharacterMapper
) : CharactersRepository {

    //TODO а вообще в репозиториях - обработка ошибки при загрузке.
    //интернет отрубился, например, в процессе загрузки, или эксепшены, от сервера 500 и пр.

    private var pageNumber = INITIAL_PAGE_NUMBER
    private var filterSettings: CharactersFilterSettings? = null
    //TODO сделать постоянное хранение настроек фильтра

//    override suspend fun getNextCharactersPage() = flow {
//        if (context.isInternetAvailable()) {
//            try {
//                val pageDto = apiService.loadPage(pageNumber)
//                charactersDao.insertList(
//                    mapper.mapPageDtoToDbModelList(pageDto, pageNumber)
//                )
//                pageNumber++
//                emit(mapper.mapPageToEntitiesList(pageDto))
//            } catch (exception: Throwable) {
//                emit(emptyList())
//            }
//        } else {
//            val dbModels = charactersDao.getPage(pageNumber)
//            if (dbModels.isNotEmpty()) {
//                pageNumber++
//                emit(mapper.mapDbModelsListToEntitiesList(dbModels))
//            } else {
//                emit(emptyList())
//            }
//        }
//    }

    override suspend fun getNextCharactersPage(): List<CharacterEntity> {
        if (context.isInternetAvailable()) {
            return try {
                val pageDto = apiService.loadPage(pageNumber)
                charactersDao.insertList(
                    mapper.mapPageDtoToDbModelList(pageDto, pageNumber)
                )
                pageNumber++
                mapper.mapPageToEntitiesList(pageDto)
            } catch (exception: Throwable) {
                emptyList()
            }
        } else {
            val dbModels = charactersDao.getPage(pageNumber)
            return if (dbModels.isNotEmpty()) {
                pageNumber++
                mapper.mapDbModelsListToEntitiesList(dbModels)
            } else {
                emptyList()
            }
        }
    }

//    override suspend fun getCharacter(characterId: Int) = flow {
//        if (context.isInternetAvailable()) {
//            val characterDto = apiService.loadItem(characterId)
//            emit(mapper.mapDtoToEntity(characterDto))
//        } else {
//            val dbModel = charactersDao.getItem(characterId)
//            emit(mapper.mapDbModelToEntity(dbModel))
//        }
//    }

    //TODO возвращать null если не удалось получить сущность по id
    override suspend fun getCharacter(characterId: Int) = if (context.isInternetAvailable()) {
        //TODO баг, почему-то возвращается 404 при запросе персонажа
        val characterDto = apiService.loadItem(characterId)
        mapper.mapDtoToEntity(characterDto)
    } else {
        val dbModel = charactersDao.getItem(characterId)
        mapper.mapDbModelToEntity(dbModel)
    }

    //TODO null при ошибке, обрабатывать если что пойдёт не так при загрузке
    override suspend fun getCharactersById(ids: List<Int>) = if (ids.isEmpty()) {
        emptyList()
    } else if (context.isInternetAvailable()) {
        val characters = apiService.loadItemsByIds(ids.joinToString(","))
        mapper.mapDtoListToEntityList(characters)
    } else {
        val characters = charactersDao.getItemsByIds(ids)
        if (characters.size < ids.size) {
            null
        } else {
            mapper.mapDbModelsListToEntitiesList(characters)
        }
    }

    override fun resetPage() {
        pageNumber = INITIAL_PAGE_NUMBER
        //TODO разрулить: сброс м.б. пока данные загружаются и прежнее значение ещё используется
    }

    override suspend fun getFilterSettings() = filterSettings ?: CharactersFilterSettings(
        EMPTY_VALUE, null, EMPTY_VALUE, EMPTY_VALUE, null
    )

    override suspend fun saveFilterSettings(settings: CharactersFilterSettings): Boolean {
        filterSettings = settings
        return true
    }

    companion object {

        private const val INITIAL_PAGE_NUMBER = 1
        private const val EMPTY_VALUE = ""
    }
}

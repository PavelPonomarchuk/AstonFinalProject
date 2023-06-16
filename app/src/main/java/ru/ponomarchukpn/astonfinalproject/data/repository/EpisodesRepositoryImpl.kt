package ru.ponomarchukpn.astonfinalproject.data.repository

import android.content.Context
import ru.ponomarchukpn.astonfinalproject.common.isInternetAvailable
import ru.ponomarchukpn.astonfinalproject.data.database.EpisodesDao
import ru.ponomarchukpn.astonfinalproject.data.mapper.EpisodeMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.EpisodesApiService
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodesFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository
import javax.inject.Inject

class EpisodesRepositoryImpl @Inject constructor(
    private val context: Context,
    private val episodesDao: EpisodesDao,
    private val apiService: EpisodesApiService,
    private val mapper: EpisodeMapper
) : EpisodesRepository {

    private var pageNumber = INITIAL_PAGE_NUMBER
    private var filterSettings: EpisodesFilterSettings? = null
    //TODO сделать постоянное хранение настроек фильтра

//    override suspend fun getNextEpisodesPage() = flow {
//        if (context.isInternetAvailable()) {
//            try {
//                val pageDto = apiService.loadPage(pageNumber)
//                episodesDao.insertList(
//                    mapper.mapPageToDbModelsList(pageDto, pageNumber)
//                )
//                pageNumber++
//                emit(mapper.mapPageToEntitiesList(pageDto))
//            } catch (exception: Throwable) {
//                emit(emptyList())
//            }
//        } else {
//            val dbModels = episodesDao.getPage(pageNumber)
//            if (dbModels.isNotEmpty()) {
//                pageNumber++
//                emit(mapper.mapDbModelListToEntityList(dbModels))
//            } else {
//                emit(emptyList())
//            }
//        }
//    }

    override suspend fun getNextEpisodesPage(): List<EpisodeEntity> {
        if (context.isInternetAvailable()) {
            return try {
                val pageDto = apiService.loadPage(pageNumber)
                episodesDao.insertList(
                    mapper.mapPageToDbModelsList(pageDto, pageNumber)
                )
                pageNumber++
                mapper.mapPageToEntitiesList(pageDto)
            } catch (exception: Throwable) {
                emptyList()
            }
        } else {
            val dbModels = episodesDao.getPage(pageNumber)
            return if (dbModels.isNotEmpty()) {
                pageNumber++
                mapper.mapDbModelListToEntityList(dbModels)
            } else {
                emptyList()
            }
        }
    }

//    override suspend fun getEpisode(episodeId: Int) = flow {
//        if (context.isInternetAvailable()) {
//            val episodeDto = apiService.loadItem(episodeId)
//            emit(mapper.mapDtoToEntity(episodeDto))
//        } else {
//            val dbModel = episodesDao.getItem(episodeId)
//            emit(mapper.mapDbModelToEntity(dbModel))
//        }
//    }

    //TODO возвращать налл если не удалось получить эпизод по ид
    override suspend fun getEpisode(episodeId: Int) = if (context.isInternetAvailable()) {
        val episodeDto = apiService.loadItem(episodeId)
        mapper.mapDtoToEntity(episodeDto)
    } else {
        val dbModel = episodesDao.getItem(episodeId)
        mapper.mapDbModelToEntity(dbModel)
    }

    //TODO тоже обработка ошибок при загрузке, возвращать null
    override suspend fun getEpisodesById(ids: List<Int>) = if (ids.isEmpty()) {
        emptyList()
    } else if (context.isInternetAvailable()) {
        val episodes = apiService.loadItemsByIds(ids.joinToString(","))
        mapper.mapDtoListToEntityList(episodes)
    } else {
        val episodes = episodesDao.getItemsByIds(ids)
        if (episodes.size < ids.size) {
            null
        } else {
            mapper.mapDbModelListToEntityList(episodes)
        }
    }

    override fun resetPage() {
        pageNumber = INITIAL_PAGE_NUMBER
        //TODO разрулить: сброс м.б. пока данные загружаются и прежнее значение ещё используется
    }

    override suspend fun getFilterSettings() = filterSettings ?: EpisodesFilterSettings(
        EMPTY_VALUE, EMPTY_VALUE
    )

    override suspend fun saveFilterSettings(settings: EpisodesFilterSettings): Boolean {
        filterSettings = settings
        return true
    }

    companion object {

        private const val INITIAL_PAGE_NUMBER = 1
        private const val EMPTY_VALUE = ""
    }
}

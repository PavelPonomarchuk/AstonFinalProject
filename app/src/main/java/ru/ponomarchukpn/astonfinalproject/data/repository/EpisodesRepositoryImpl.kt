package ru.ponomarchukpn.astonfinalproject.data.repository

import android.content.Context
import kotlinx.coroutines.flow.flow
import ru.ponomarchukpn.astonfinalproject.common.isInternetAvailable
import ru.ponomarchukpn.astonfinalproject.data.database.EpisodesDao
import ru.ponomarchukpn.astonfinalproject.data.mapper.EpisodeMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.EpisodesApiService
import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository
import javax.inject.Inject

class EpisodesRepositoryImpl @Inject constructor(
    private val context: Context,
    private val episodesDao: EpisodesDao,
    private val apiService: EpisodesApiService,
    private val mapper: EpisodeMapper
) : EpisodesRepository {

    private var pageNumber = INITIAL_PAGE_NUMBER

    override suspend fun getNextEpisodesPage() = flow {
        if (context.isInternetAvailable()) {
            try {
                val pageDto = apiService.loadPage(pageNumber)
                episodesDao.insertList(
                    mapper.mapPageToDbModelsList(pageDto, pageNumber)
                )
                pageNumber++
                emit(mapper.mapPageToEntitiesList(pageDto))
            } catch (exception: Throwable) {
                emit(emptyList())
            }
        } else {
            val dbModels = episodesDao.getPage(pageNumber)
            if (dbModels.isNotEmpty()) {
                pageNumber++
                emit(mapper.mapDbModelListToEntityList(dbModels))
            } else {
                emit(emptyList())
            }
        }
    }

    override suspend fun getEpisode(episodeId: Int) = flow {
        if (context.isInternetAvailable()) {
            val episodeDto = apiService.loadItem(episodeId)
            emit(mapper.mapDtoToEntity(episodeDto))
        } else {
            val dbModel = episodesDao.getItem(episodeId)
            emit(mapper.mapDbModelToEntity(dbModel))
        }
    }

    companion object {

        private const val INITIAL_PAGE_NUMBER = 1
    }
}

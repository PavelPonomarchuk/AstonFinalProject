package ru.ponomarchukpn.astonfinalproject.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodesFilterSettings

interface EpisodesRepository {

    fun getEpisodes(): Flow<List<EpisodeEntity>>

    suspend fun loadEpisodesPage(pageNumber: Int): Boolean

    fun getEpisode(episodeId: Int): Flow<EpisodeEntity>

    fun getEpisodesById(ids: List<Int>): Flow<List<EpisodeEntity>>

    suspend fun loadEpisodesById(ids: List<Int>): Boolean

    suspend fun getFilterSettings(): EpisodesFilterSettings

    suspend fun saveFilterSettings(settings: EpisodesFilterSettings): Boolean
}

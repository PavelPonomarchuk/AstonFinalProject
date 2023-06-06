package ru.ponomarchukpn.astonfinalproject.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity

interface EpisodesRepository {

    suspend fun getNextEpisodesPage(): Flow<List<EpisodeEntity>>

    suspend fun getEpisode(episodeId: Int): Flow<EpisodeEntity>
}

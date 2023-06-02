package ru.ponomarchukpn.astonfinalproject.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity

interface EpisodesRepository {

    fun getNextEpisodesPage(): Flow<List<EpisodeEntity>>

    fun getEpisode(episodeId: Int): Flow<EpisodeEntity>
}

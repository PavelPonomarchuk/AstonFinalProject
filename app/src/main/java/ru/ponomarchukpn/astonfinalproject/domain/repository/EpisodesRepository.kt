package ru.ponomarchukpn.astonfinalproject.domain.repository

import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodesFilterSettings

interface EpisodesRepository {

    suspend fun getNextEpisodesPage(): List<EpisodeEntity>

    suspend fun getEpisode(episodeId: Int): EpisodeEntity?

    suspend fun getEpisodesById(ids: List<Int>): List<EpisodeEntity>?

    suspend fun getFilterSettings(): EpisodesFilterSettings

    suspend fun saveFilterSettings(settings: EpisodesFilterSettings): Boolean

    fun resetPage()
}

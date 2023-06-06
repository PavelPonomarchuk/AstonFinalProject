package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository

class GetSingleEpisodeUseCase(
    private val repository: EpisodesRepository
) {

    suspend operator fun invoke(episodeId: Int) = repository.getEpisode(episodeId)
}

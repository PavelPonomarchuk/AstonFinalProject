package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository

class GetSingleEpisodeUseCase(
    private val repository: EpisodesRepository
) {

    operator fun invoke(episodeId: Int) = repository.getEpisode(episodeId)
}

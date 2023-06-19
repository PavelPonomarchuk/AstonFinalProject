package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository
import javax.inject.Inject

class GetSingleEpisodeUseCase @Inject constructor(
    private val repository: EpisodesRepository
) {

    suspend operator fun invoke(episodeId: Int) = repository.getEpisode(episodeId)
}

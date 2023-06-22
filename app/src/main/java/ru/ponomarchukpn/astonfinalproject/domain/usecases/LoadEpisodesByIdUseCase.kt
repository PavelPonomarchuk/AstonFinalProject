package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository
import javax.inject.Inject

class LoadEpisodesByIdUseCase @Inject constructor(
    private val repository: EpisodesRepository
) {

    suspend operator fun invoke(ids: List<Int>) = repository.loadEpisodesById(ids)
}

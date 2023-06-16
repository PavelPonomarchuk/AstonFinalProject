package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository

class GetEpisodesByIdUseCase(
    private val repository: EpisodesRepository
) {

    suspend operator fun invoke(ids: List<Int>) = repository.getEpisodesById(ids)
}

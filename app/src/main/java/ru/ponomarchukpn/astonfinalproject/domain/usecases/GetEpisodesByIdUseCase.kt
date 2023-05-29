package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository
import javax.inject.Inject

class GetEpisodesByIdUseCase @Inject constructor(
    private val repository: EpisodesRepository
) {

    operator fun invoke(ids: List<Int>) = repository.getEpisodesById(ids)
}

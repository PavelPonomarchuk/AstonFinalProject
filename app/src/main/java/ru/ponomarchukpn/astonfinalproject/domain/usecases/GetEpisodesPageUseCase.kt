package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository

class GetEpisodesPageUseCase(
    private val repository: EpisodesRepository
) {

    suspend operator fun invoke() = repository.getNextEpisodesPage()
}

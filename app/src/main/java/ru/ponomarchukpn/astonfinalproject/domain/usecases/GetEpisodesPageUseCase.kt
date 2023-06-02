package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository

class GetEpisodesPageUseCase(
    private val repository: EpisodesRepository
) {

    operator fun invoke() = repository.getNextEpisodesPage()
}

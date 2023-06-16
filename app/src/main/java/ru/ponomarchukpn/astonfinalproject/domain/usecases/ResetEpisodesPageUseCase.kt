package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository

class ResetEpisodesPageUseCase(
    private val repository: EpisodesRepository
) {

    operator fun invoke() = repository.resetPage()
}

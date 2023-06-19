package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository
import javax.inject.Inject

class ResetEpisodesPageUseCase @Inject constructor(
    private val repository: EpisodesRepository
) {

    operator fun invoke() = repository.resetPage()
}

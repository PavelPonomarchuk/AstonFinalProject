package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository
import javax.inject.Inject

class GetEpisodesPageUseCase @Inject constructor(
    private val repository: EpisodesRepository
) {

    suspend operator fun invoke() = repository.getNextEpisodesPage()
}

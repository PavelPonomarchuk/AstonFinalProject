package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodesFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository
import javax.inject.Inject

class SaveEpisodesFilterUseCase @Inject constructor(
    private val repository: EpisodesRepository
) {

    suspend operator fun invoke(settings: EpisodesFilterSettings) =
        repository.saveFilterSettings(settings)
}

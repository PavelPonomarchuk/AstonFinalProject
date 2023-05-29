package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class SaveCharactersFilterUseCase @Inject constructor(
    private val repository: CharactersRepository
) {

    suspend operator fun invoke(settings: CharactersFilterSettings) =
        repository.saveFilterSettings(settings)
}

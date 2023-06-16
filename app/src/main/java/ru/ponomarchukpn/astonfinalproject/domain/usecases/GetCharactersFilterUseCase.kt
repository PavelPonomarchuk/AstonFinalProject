package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository

class GetCharactersFilterUseCase(
    private val repository: CharactersRepository
) {

    suspend operator fun invoke() = repository.getFilterSettings()
}

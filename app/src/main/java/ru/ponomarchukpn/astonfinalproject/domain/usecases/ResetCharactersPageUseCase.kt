package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository

class ResetCharactersPageUseCase(
    private val repository: CharactersRepository
) {

    operator fun invoke() = repository.resetPage()
}

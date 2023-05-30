package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository

class GetCharactersPageUseCase(
    private val repository: CharactersRepository
) {

    operator fun invoke() = repository.getNextCharactersPage()
}

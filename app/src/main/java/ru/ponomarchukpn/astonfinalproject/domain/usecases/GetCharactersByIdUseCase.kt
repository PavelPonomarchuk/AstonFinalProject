package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository

class GetCharactersByIdUseCase(
    private val repository: CharactersRepository
) {

    suspend operator fun invoke(ids: List<Int>) = repository.getCharactersById(ids)
}

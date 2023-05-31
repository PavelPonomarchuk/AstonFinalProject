package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository

class GetSingleCharacterUseCase(
    private val repository: CharactersRepository
) {

    operator fun invoke(characterId: Int) = repository.getCharacter(characterId)
}

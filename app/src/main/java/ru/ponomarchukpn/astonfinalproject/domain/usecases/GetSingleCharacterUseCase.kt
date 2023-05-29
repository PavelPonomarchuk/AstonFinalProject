package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class GetSingleCharacterUseCase @Inject constructor(
    private val repository: CharactersRepository
) {

    operator fun invoke(characterId: Int) = repository.getCharacter(characterId)
}

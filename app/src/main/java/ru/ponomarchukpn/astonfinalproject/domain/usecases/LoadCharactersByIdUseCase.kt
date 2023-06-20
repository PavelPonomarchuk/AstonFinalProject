package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class LoadCharactersByIdUseCase @Inject constructor(
    private val repository: CharactersRepository
) {

    suspend operator fun invoke(ids: List<Int>) = repository.loadCharactersById(ids)
}

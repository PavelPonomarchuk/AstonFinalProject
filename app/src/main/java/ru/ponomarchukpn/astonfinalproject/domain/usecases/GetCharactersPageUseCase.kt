package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class GetCharactersPageUseCase @Inject constructor(
    private val repository: CharactersRepository
) {

    suspend operator fun invoke() = repository.getNextCharactersPage()
}

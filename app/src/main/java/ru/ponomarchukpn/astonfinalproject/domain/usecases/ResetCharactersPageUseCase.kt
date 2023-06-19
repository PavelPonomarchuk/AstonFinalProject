package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class ResetCharactersPageUseCase @Inject constructor(
    private val repository: CharactersRepository
) {

    operator fun invoke() = repository.resetPage()
}

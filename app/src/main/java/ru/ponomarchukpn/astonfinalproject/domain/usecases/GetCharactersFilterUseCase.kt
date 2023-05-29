package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class GetCharactersFilterUseCase @Inject constructor(
    private val repository: CharactersRepository
) {

    suspend operator fun invoke() = repository.getFilterSettings()
}

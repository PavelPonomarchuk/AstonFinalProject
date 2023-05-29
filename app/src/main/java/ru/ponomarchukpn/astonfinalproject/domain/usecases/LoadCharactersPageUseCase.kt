package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class LoadCharactersPageUseCase @Inject constructor(
    private val repository: CharactersRepository
){

    suspend operator fun invoke(pageNumber: Int) = repository.loadCharactersPage(pageNumber)
}

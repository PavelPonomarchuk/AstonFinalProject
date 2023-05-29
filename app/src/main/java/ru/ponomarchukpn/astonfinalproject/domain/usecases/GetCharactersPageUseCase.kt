package ru.ponomarchukpn.astonfinalproject.domain.usecases

import ru.ponomarchukpn.astonfinalproject.data.repository.CharactersRepositoryImpl
import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository

//TODO инжектить репозиторий
class GetCharactersPageUseCase(
    private val repository: CharactersRepository = CharactersRepositoryImpl()
) {

    operator fun invoke(pageNumber: Int) = repository.getCharactersPage(pageNumber)
}

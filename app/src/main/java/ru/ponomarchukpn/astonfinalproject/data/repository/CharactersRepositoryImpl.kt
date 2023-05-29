package ru.ponomarchukpn.astonfinalproject.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.ponomarchukpn.astonfinalproject.data.mapper.CharacterMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.ApiFactory
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository

//TODO инжектить маппер, дао и аппликейшн если надо, ретрофит
class CharactersRepositoryImpl : CharactersRepository {

    override fun getCharactersPage(pageNumber: Int) = flow {
        if (isInternetAvailable()) {
            val dto = ApiFactory.charactersApiService.loadCharactersPage(pageNumber)
            //сохранять данные в БД
            emit(CharacterMapper.mapCharactersPageToResponseEntity(dto))
        } else {
            //таким же образом получать из БД и возвращать
        }
    }

    override fun getCharacter(characterId: Int): Flow<CharacterEntity> {
        TODO("Not yet implemented")
    }

    private fun isInternetAvailable(): Boolean {
        //TODO
        return false
    }
}

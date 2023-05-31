package ru.ponomarchukpn.astonfinalproject.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ponomarchukpn.astonfinalproject.data.network.dto.CharacterDto
import ru.ponomarchukpn.astonfinalproject.data.network.dto.CharactersPageDto

interface CharactersApiService {

    @GET("character")
    suspend fun loadCharactersPage(
        @Query(QUERY_PARAM_PAGE) page: Int
    ): CharactersPageDto

    @GET("character/{characterId}")
    suspend fun loadSingleCharacter(
        @Path(PATH_CHARACTER_ID) characterId: Int
    ): CharacterDto

    companion object {

        private const val QUERY_PARAM_PAGE = "page"
        private const val PATH_CHARACTER_ID = "characterId"
    }
}

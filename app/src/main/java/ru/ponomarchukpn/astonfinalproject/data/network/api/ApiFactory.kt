package ru.ponomarchukpn.astonfinalproject.data.network.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val charactersApiService: CharactersApiService =
        retrofit.create(CharactersApiService::class.java)

    val locationsApiService: LocationsApiService = retrofit.create(LocationsApiService::class.java)
    val episodesApiService: EpisodesApiService = retrofit.create(EpisodesApiService::class.java)
}

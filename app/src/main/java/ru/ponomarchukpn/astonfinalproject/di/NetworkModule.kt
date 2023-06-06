package ru.ponomarchukpn.astonfinalproject.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ponomarchukpn.astonfinalproject.data.network.api.CharactersApiService
import ru.ponomarchukpn.astonfinalproject.data.network.api.EpisodesApiService
import ru.ponomarchukpn.astonfinalproject.data.network.api.LocationsApiService
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideCharactersApiService(retrofit: Retrofit): CharactersApiService =
        retrofit.create(CharactersApiService::class.java)

    @Provides
    @Singleton
    fun provideLocationsApiService(retrofit: Retrofit): LocationsApiService =
        retrofit.create(LocationsApiService::class.java)

    @Provides
    @Singleton
    fun provideEpisodesApiService(retrofit: Retrofit): EpisodesApiService =
        retrofit.create(EpisodesApiService::class.java)

    companion object {

        private const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}

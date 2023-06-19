package ru.ponomarchukpn.astonfinalproject.di

import dagger.Binds
import dagger.Module
import ru.ponomarchukpn.astonfinalproject.data.repository.CharactersRepositoryImpl
import ru.ponomarchukpn.astonfinalproject.data.repository.EpisodesRepositoryImpl
import ru.ponomarchukpn.astonfinalproject.data.repository.LocationsRepositoryImpl
import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository
import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository
import javax.inject.Singleton

@Module
interface DomainModule {

    @Binds
    @Singleton
    fun bindCharactersRepository(impl: CharactersRepositoryImpl): CharactersRepository

    @Binds
    @Singleton
    fun bindLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository

    @Binds
    @Singleton
    fun bindEpisodesRepository(impl: EpisodesRepositoryImpl): EpisodesRepository
}

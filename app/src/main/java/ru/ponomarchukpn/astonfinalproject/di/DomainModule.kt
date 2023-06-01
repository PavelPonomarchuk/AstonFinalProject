package ru.ponomarchukpn.astonfinalproject.di

import dagger.Module
import dagger.Provides
import ru.ponomarchukpn.astonfinalproject.data.repository.CharactersRepositoryImpl
import ru.ponomarchukpn.astonfinalproject.data.repository.LocationsRepositoryImpl
import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetLocationsPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleCharacterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleLocationUseCase
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideCharactersRepository(impl: CharactersRepositoryImpl): CharactersRepository = impl

    @Provides
    @Singleton
    fun provideCharactersPageUseCase(repository: CharactersRepository) = GetCharactersPageUseCase(
        repository
    )

    @Provides
    @Singleton
    fun provideSingleCharacterUseCase(repository: CharactersRepository) = GetSingleCharacterUseCase(
        repository
    )

    @Provides
    @Singleton
    fun provideLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository = impl

    @Provides
    @Singleton
    fun provideLocationsPageUseCase(repository: LocationsRepository) = GetLocationsPageUseCase(
        repository
    )

    @Provides
    @Singleton
    fun provideSingleLocationUseCase(repository: LocationsRepository) = GetSingleLocationUseCase(
        repository
    )
}

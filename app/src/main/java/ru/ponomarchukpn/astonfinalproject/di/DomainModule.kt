package ru.ponomarchukpn.astonfinalproject.di

import dagger.Module
import dagger.Provides
import ru.ponomarchukpn.astonfinalproject.data.repository.CharactersRepositoryImpl
import ru.ponomarchukpn.astonfinalproject.data.repository.EpisodesRepositoryImpl
import ru.ponomarchukpn.astonfinalproject.data.repository.LocationsRepositoryImpl
import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository
import ru.ponomarchukpn.astonfinalproject.domain.repository.LocationsRepository
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersByIdUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetEpisodesByIdUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetEpisodesFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetEpisodesPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetLocationNameUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetLocationsFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetLocationsPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleCharacterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleEpisodeUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleLocationUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.ResetCharactersPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.ResetEpisodesPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.ResetLocationsPageUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.SaveCharactersFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.SaveEpisodesFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.SaveLocationsFilterUseCase
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideCharactersRepository(impl: CharactersRepositoryImpl): CharactersRepository = impl

    //TODO попробовать убрать портянку, инжект на конструкторе в юзкейсах

    @Provides
    @Singleton
    fun provideCharactersPageUseCase(repository: CharactersRepository) =
        GetCharactersPageUseCase(repository)

    @Provides
    @Singleton
    fun provideSingleCharacterUseCase(repository: CharactersRepository) =
        GetSingleCharacterUseCase(repository)

    @Provides
    @Singleton
    fun provideCharactersByIdUseCase(repository: CharactersRepository) =
        GetCharactersByIdUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCharactersFilterUseCase(repository: CharactersRepository) =
        GetCharactersFilterUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveCharactersFilterUseCase(repository: CharactersRepository) =
        SaveCharactersFilterUseCase(repository)

    @Provides
    @Singleton
    fun provideResetCharactersPageUseCase(repository: CharactersRepository) =
        ResetCharactersPageUseCase(repository)

    @Provides
    @Singleton
    fun provideLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository = impl

    @Provides
    @Singleton
    fun provideLocationsPageUseCase(repository: LocationsRepository) =
        GetLocationsPageUseCase(repository)

    @Provides
    @Singleton
    fun provideSingleLocationUseCase(repository: LocationsRepository) =
        GetSingleLocationUseCase(repository)

    @Provides
    @Singleton
    fun provideLocationNameUseCase(repository: LocationsRepository) =
        GetLocationNameUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLocationFilterUseCase(repository: LocationsRepository) =
        GetLocationsFilterUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveLocationsFilterUseCase(repository: LocationsRepository) =
        SaveLocationsFilterUseCase(repository)

    @Provides
    @Singleton
    fun provideResetLocationsPageUseCase(repository: LocationsRepository) =
        ResetLocationsPageUseCase(repository)

    @Provides
    @Singleton
    fun provideEpisodesRepository(impl: EpisodesRepositoryImpl): EpisodesRepository = impl

    @Provides
    @Singleton
    fun provideEpisodesPageUseCase(repository: EpisodesRepository) =
        GetEpisodesPageUseCase(repository)

    @Provides
    @Singleton
    fun provideSingleEpisodeUseCase(repository: EpisodesRepository) =
        GetSingleEpisodeUseCase(repository)

    @Provides
    @Singleton
    fun provideResetEpisodesPageUseCase(repository: EpisodesRepository) =
        ResetEpisodesPageUseCase(repository)

    @Provides
    @Singleton
    fun provideGetEpisodesByIdUseCase(repository: EpisodesRepository) =
        GetEpisodesByIdUseCase(repository)

    @Provides
    @Singleton
    fun provideGetEpisodesFilterUseCase(repository: EpisodesRepository) =
        GetEpisodesFilterUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveEpisodesFilterUseCase(repository: EpisodesRepository) =
        SaveEpisodesFilterUseCase(repository)
}

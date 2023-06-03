package ru.ponomarchukpn.astonfinalproject.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.ponomarchukpn.astonfinalproject.data.database.AppDatabase
import ru.ponomarchukpn.astonfinalproject.data.network.api.ApiFactory
import javax.inject.Singleton

@Module
class DataModule {

    //TODO убрать БД когда поправлю CharactersRepositoryImpl
    @Provides
    @Singleton
    fun provideDatabase(context: Context) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideCharactersApiService() = ApiFactory.charactersApiService

    @Provides
    @Singleton
    fun provideLocationsDao(context: Context) = AppDatabase.getInstance(context).locationsDao()

    @Provides
    @Singleton
    fun provideLocationsApiService() = ApiFactory.locationsApiService

    @Provides
    @Singleton
    fun provideEpisodesDao(context: Context) = AppDatabase.getInstance(context).episodesDao()

    @Provides
    @Singleton
    fun provideEpisodesApiService() = ApiFactory.episodesApiService
}

package ru.ponomarchukpn.astonfinalproject.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.ponomarchukpn.astonfinalproject.data.database.AppDatabase
import ru.ponomarchukpn.astonfinalproject.data.network.api.ApiFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun providesCharactersApiService() = ApiFactory.charactersApiService
}

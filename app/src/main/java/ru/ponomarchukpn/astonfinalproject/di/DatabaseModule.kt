package ru.ponomarchukpn.astonfinalproject.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.ponomarchukpn.astonfinalproject.data.database.AppDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DB_NAME
    ).build()

    @Provides
    @Singleton
    fun provideCharactersDao(database: AppDatabase) = database.charactersDao()

    @Provides
    @Singleton
    fun provideLocationsDao(database: AppDatabase) = database.locationsDao()

    @Provides
    @Singleton
    fun provideEpisodesDao(database: AppDatabase) = database.episodesDao()

    companion object {

        private const val DB_NAME = "main.db"
    }
}

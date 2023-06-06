package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CharacterDbModel::class, LocationDbModel::class, EpisodeDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun charactersDao(): CharactersDao
    abstract fun locationsDao(): LocationsDao
    abstract fun episodesDao(): EpisodesDao
}

package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = EpisodesDao.TABLE_NAME)
data class EpisodeDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val charactersId: String,
    val url: String,
    val created: String
)

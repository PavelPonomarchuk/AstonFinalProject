package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CharactersDao.TABLE_NAME)
data class CharacterDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: Int,
    val species: String,
    val type: String,
    val gender: Int,
    val originId: Int,
    val locationId: Int,
    val imageUrl: String,
    val episodesId: String,
    val url: String,
    val created: String
)

package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocationsDao.TABLE_NAME)
data class LocationDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residentsId: String,
    val url: String,
    val created: String
)

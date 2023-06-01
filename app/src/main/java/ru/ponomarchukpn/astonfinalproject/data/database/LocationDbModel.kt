package ru.ponomarchukpn.astonfinalproject.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residentsId: String,
    val url: String,
    val created: String,
    val relatedToPage: Int
)

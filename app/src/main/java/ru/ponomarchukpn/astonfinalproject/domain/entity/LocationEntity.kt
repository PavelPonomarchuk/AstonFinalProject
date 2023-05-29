package ru.ponomarchukpn.astonfinalproject.domain.entity

data class LocationEntity(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residentsId: List<Int>,
    val url: String,
    val created: String
)

package ru.ponomarchukpn.astonfinalproject.domain.entity

data class EpisodeEntity(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val charactersId: List<Int>,
    val url: String,
    val created: String
)

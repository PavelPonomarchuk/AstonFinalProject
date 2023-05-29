package ru.ponomarchukpn.astonfinalproject.domain.entity

data class CharacterEntity(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val originId: Int,
    val locationId: Int,
    val imageUrl: String,
    val episodesId: List<Int>,
    val url: String,
    val created: String
)

package ru.ponomarchukpn.astonfinalproject.domain.entity

data class CharactersFilterSettings(
    val name: String,
    val status: CharacterStatus?,
    val species: String,
    val type: String,
    val gender: CharacterGender?
)

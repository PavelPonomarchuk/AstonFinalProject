package ru.ponomarchukpn.astonfinalproject.domain.entity

data class CharactersPageResponse(
    val hasNextPage: Boolean,
    val characters: List<CharacterEntity>
)

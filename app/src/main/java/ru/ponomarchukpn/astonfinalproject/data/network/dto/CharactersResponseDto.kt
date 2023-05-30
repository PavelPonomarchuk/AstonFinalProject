package ru.ponomarchukpn.astonfinalproject.data.network.dto

import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity

data class CharactersResponseDto(
    val hasNextPage: Boolean,
    val characters: List<CharacterEntity>
)

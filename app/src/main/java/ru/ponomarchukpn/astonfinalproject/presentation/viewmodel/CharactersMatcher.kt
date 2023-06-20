package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersFilterSettings
import javax.inject.Inject

class CharactersMatcher @Inject constructor() {

    fun isCharacterMatches(filter: CharactersFilterSettings, character: CharacterEntity): Boolean {
        val nameMatches = if (filter.name != EMPTY_STRING) {
            character.name.contains(filter.name)
        } else {
            true
        }

        val statusMatches = filter.status?.let {
            character.status == it
        } ?: true

        val speciesMatches = if (filter.species != EMPTY_STRING) {
            character.species.contains(filter.species)
        } else {
            true
        }

        val typeMatches = if (filter.type != EMPTY_STRING) {
            character.type.contains(filter.type)
        } else {
            true
        }

        val genderMatches = filter.gender?.let {
            character.gender == it
        } ?: true

        return nameMatches && statusMatches && speciesMatches && typeMatches && genderMatches
    }

    companion object {

        private const val EMPTY_STRING = ""
    }
}

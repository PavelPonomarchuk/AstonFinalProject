package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationsFilterSettings
import javax.inject.Inject

class LocationsMatcher @Inject constructor() {

    fun isLocationMatches(filter: LocationsFilterSettings, location: LocationEntity): Boolean {
        val nameMatches = if (filter.name != EMPTY_STRING) {
            location.name.contains(filter.name, true)
        } else {
            true
        }

        val typeMatches = if (filter.type != EMPTY_STRING) {
            location.type.contains(filter.type, true)
        } else {
            true
        }

        val dimensionMatches = if (filter.dimension != EMPTY_STRING) {
            location.dimension.contains(filter.dimension, true)
        } else {
            true
        }

        return nameMatches && typeMatches && dimensionMatches
    }

    companion object {

        private const val EMPTY_STRING = ""
    }
}

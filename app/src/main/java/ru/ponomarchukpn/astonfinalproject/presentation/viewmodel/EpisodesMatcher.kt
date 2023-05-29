package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodesFilterSettings
import javax.inject.Inject

class EpisodesMatcher @Inject constructor() {

    fun isEpisodeMatches(filter: EpisodesFilterSettings, episode: EpisodeEntity): Boolean {
        val nameMatches = if (filter.name != EMPTY_STRING) {
            episode.name.contains(filter.name, true)
        } else {
            true
        }

        val episodeCodeMatches = if (filter.code != EMPTY_STRING) {
            episode.episode.contains(filter.code, true)
        } else {
            true
        }

        return nameMatches && episodeCodeMatches
    }

    companion object {

        private const val EMPTY_STRING = ""
    }
}

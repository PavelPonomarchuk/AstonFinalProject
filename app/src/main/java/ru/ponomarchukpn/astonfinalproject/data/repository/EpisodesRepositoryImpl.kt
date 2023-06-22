package ru.ponomarchukpn.astonfinalproject.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import ru.ponomarchukpn.astonfinalproject.data.database.EpisodesDao
import ru.ponomarchukpn.astonfinalproject.data.mapper.EpisodeMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.EpisodesApiService
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodesFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.repository.EpisodesRepository
import javax.inject.Inject

class EpisodesRepositoryImpl @Inject constructor(
    private val context: Context,
    private val episodesDao: EpisodesDao,
    private val apiService: EpisodesApiService,
    private val mapper: EpisodeMapper
) : EpisodesRepository {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        EPISODES_PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    override suspend fun getFilterSettings(): EpisodesFilterSettings {
        val json = preferences.getString(KEY_EPISODES_FILTER, null)
        return json?.let {
            Gson().fromJson(json, EpisodesFilterSettings::class.java)
        } ?: EpisodesFilterSettings(EMPTY_VALUE, EMPTY_VALUE)
    }

    override suspend fun saveFilterSettings(settings: EpisodesFilterSettings): Boolean {
        val json = Gson().toJson(settings)
        preferences.edit().putString(KEY_EPISODES_FILTER, json).apply()
        return true
    }

    override fun getEpisodes() = episodesDao.getAll().map {
        mapper.mapDbModelListToEntityList(it)
    }

    override suspend fun loadEpisodesPage(pageNumber: Int): Boolean {
        return try {
            val pageDto = apiService.loadPage(pageNumber)
            episodesDao.insertList(
                mapper.mapPageToDbModelsList(pageDto)
            )
            true
        } catch (exception: Throwable) {
            false
        }
    }

    override fun getEpisode(episodeId: Int) = episodesDao.getItem(episodeId).map {
        mapper.mapDbModelToEntity(it)
    }

    override fun getEpisodesById(ids: List<Int>) = episodesDao.getItemsByIds(ids).map {
        mapper.mapDbModelListToEntityList(it)
    }

    override suspend fun loadEpisodesById(ids: List<Int>): Boolean {
        if (ids.isEmpty()) {
            return true
        }
        return try {
            val episodes = apiService.loadItemsByIds(ids.joinToString(","))
            episodesDao.insertList(
                mapper.mapDtoListToDbModelList(episodes)
            )
            true
        } catch (exception: Throwable) {
            false
        }
    }

    companion object {

        private const val EPISODES_PREFERENCES_NAME = "episodesRepositoryPreferences"
        private const val KEY_EPISODES_FILTER = "episodesFilter"
        private const val EMPTY_VALUE = ""
    }
}

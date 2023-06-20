package ru.ponomarchukpn.astonfinalproject.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import ru.ponomarchukpn.astonfinalproject.data.database.CharactersDao
import ru.ponomarchukpn.astonfinalproject.data.mapper.CharacterMapper
import ru.ponomarchukpn.astonfinalproject.data.network.api.CharactersApiService
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.repository.CharactersRepository
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val context: Context,
    private val charactersDao: CharactersDao,
    private val apiService: CharactersApiService,
    private val mapper: CharacterMapper
) : CharactersRepository {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        CHARACTERS_PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    override fun getCharacters() = charactersDao.getAll().map {
        mapper.mapDbModelsListToEntitiesList(it)
    }

    override suspend fun loadCharactersPage(pageNumber: Int): Boolean {
        return try {
            val pageDto = apiService.loadPage(pageNumber)
            charactersDao.insertList(
                mapper.mapPageDtoToDbModelList(pageDto)
            )
            true
        } catch (exception: Throwable) {
            false
        }
    }

    override fun getCharacter(characterId: Int) = charactersDao.getItem(characterId).map {
        mapper.mapDbModelToEntity(it)
    }

    override fun getCharactersById(ids: List<Int>) = charactersDao.getItemsByIds(ids).map {
        mapper.mapDbModelsListToEntitiesList(it)
    }

    override suspend fun loadCharactersById(ids: List<Int>): Boolean {
        return try {
            val characters = apiService.loadItemsByIds(ids.joinToString(","))
            charactersDao.insertList(
                mapper.mapDtoListToDbModelList(characters)
            )
            true
        } catch (exception: Throwable) {
            false
        }
    }

    override suspend fun getFilterSettings(): CharactersFilterSettings {
        val json = preferences.getString(KEY_CHARACTERS_FILTER, null)
        return json?.let {
            Gson().fromJson(json, CharactersFilterSettings::class.java)
        } ?: CharactersFilterSettings(
            EMPTY_VALUE, null, EMPTY_VALUE, EMPTY_VALUE, null
        )
    }

    override suspend fun saveFilterSettings(settings: CharactersFilterSettings): Boolean {
        val json = Gson().toJson(settings)
        preferences.edit().putString(KEY_CHARACTERS_FILTER, json).apply()
        return true
    }

    companion object {

        private const val CHARACTERS_PREFERENCES_NAME = "charactersRepositoryPreferences"
        private const val KEY_CHARACTERS_FILTER = "charactersFilter"
        private const val EMPTY_VALUE = ""
    }
}

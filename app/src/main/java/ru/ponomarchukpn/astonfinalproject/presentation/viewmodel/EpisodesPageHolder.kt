package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class EpisodesPageHolder @Inject constructor(
    context: Context
) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    fun currentPageNumber() = preferences.getInt(KEY_EPISODES_PAGE_NUMBER, DEFAULT_PAGE_NUMBER)

    fun savePageNumber(pageNumber: Int) {
        preferences.edit().putInt(KEY_EPISODES_PAGE_NUMBER, pageNumber).apply()
    }

    companion object {

        private const val PREFERENCES_NAME = "episodesPagePreferences"
        private const val KEY_EPISODES_PAGE_NUMBER = "episodesPageNumber"
        private const val DEFAULT_PAGE_NUMBER = 1
    }
}

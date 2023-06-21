package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class LocationsPageHolder @Inject constructor(
    context: Context
) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    fun currentPageNumber() = preferences.getInt(KEY_LOCATIONS_PAGE_NUMBER, DEFAULT_PAGE_NUMBER)

    fun savePageNumber(pageNumber: Int) {
        preferences.edit().putInt(KEY_LOCATIONS_PAGE_NUMBER, pageNumber).apply()
    }

    companion object {

        private const val PREFERENCES_NAME = "locationsPagePreferences"
        private const val KEY_LOCATIONS_PAGE_NUMBER = "locationsPageNumber"
        private const val DEFAULT_PAGE_NUMBER = 1
    }
}

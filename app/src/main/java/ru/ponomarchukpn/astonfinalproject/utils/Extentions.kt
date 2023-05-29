package ru.ponomarchukpn.astonfinalproject.utils

import com.google.gson.JsonArray
import com.google.gson.JsonObject

private const val KEY_URL = "url"
private const val ABSENT_LOCATION_ID = -1

fun JsonObject.getLocationId() = get(KEY_URL).asString.let {
    if (it.isNotEmpty()) {
        it.split("/").last().toInt()
    } else {
        ABSENT_LOCATION_ID
    }
}

fun JsonArray.getIdListFromUrls() = toList().map {
    it.asString.split("/").last().toInt()
}

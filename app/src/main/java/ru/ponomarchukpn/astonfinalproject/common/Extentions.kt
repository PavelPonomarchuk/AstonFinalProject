package ru.ponomarchukpn.astonfinalproject.common

import com.google.gson.JsonArray
import com.google.gson.JsonObject

private const val KEY_URL = "url"
private const val KEY_NEXT = "next"
private const val ABSENT_LOCATION_ID = -1

fun JsonObject.getLocationIdFromUrl() = get(KEY_URL).asString.let {
    if (it.isNotEmpty()) {
        it.split("/").last().toInt()
    } else {
        ABSENT_LOCATION_ID
    }
}

fun JsonObject.hasNextPage() = get(KEY_NEXT).asString != null

fun JsonArray.getIdListFromUrls() = toList().map {
    it.asString.split("/").last().toInt()
}

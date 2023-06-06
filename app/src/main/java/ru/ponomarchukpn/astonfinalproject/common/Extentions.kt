package ru.ponomarchukpn.astonfinalproject.common

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject

private const val KEY_NEXT = "next"
private const val ABSENT_LOCATION_ID = -1

fun String.idUrlEndsWith() = if (isNotEmpty()) {
        split("/").last().toInt()
    } else {
        ABSENT_LOCATION_ID
    }

//TODO убрать если не понадобится
fun JsonObject.hasNextPage() = get(KEY_NEXT).asString != null

fun JsonArray.getIdListFromUrls() = toList().map {
    it.asString.split("/").last().toInt()
}

fun Context.isInternetAvailable(): Boolean {
    //TODO земенить deprecated функционал

    val connectivityManager = getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
    return connectivityManager.activeNetworkInfo?.isConnected ?: false
}

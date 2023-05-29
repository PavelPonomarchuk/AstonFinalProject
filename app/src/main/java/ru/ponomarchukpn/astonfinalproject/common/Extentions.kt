package ru.ponomarchukpn.astonfinalproject.common

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonArray

private const val ABSENT_LOCATION_ID = -1

fun String.idUrlEndsWith() = if (isNotEmpty()) {
        split("/").last().toInt()
    } else {
        ABSENT_LOCATION_ID
    }

fun JsonArray.getIdListFromUrls() = toList().map {
    it.asString.split("/").last().toInt()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

package ru.ponomarchukpn.astonfinalproject.data.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationMinimalDto(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("url")
    @Expose
    val url: String
)

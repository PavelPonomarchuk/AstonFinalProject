package ru.ponomarchukpn.astonfinalproject.data.network.dto

import com.google.gson.JsonArray
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EpisodeDto(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("air_date")
    @Expose
    val airDate: String,
    @SerializedName("episode")
    @Expose
    val episode: String,
    @SerializedName("characters")
    @Expose
    val characters: JsonArray,
    @SerializedName("url")
    @Expose
    val url: String,
    @SerializedName("created")
    @Expose
    val created: String
)

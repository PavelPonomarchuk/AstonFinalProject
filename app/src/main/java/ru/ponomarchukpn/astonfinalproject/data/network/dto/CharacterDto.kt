package ru.ponomarchukpn.astonfinalproject.data.network.dto

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CharacterDto(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("species")
    @Expose
    val species: String,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("gender")
    @Expose
    val gender: String,
    @SerializedName("origin")
    @Expose
    val origin: JsonObject,
    @SerializedName("location")
    @Expose
    val location: JsonObject,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("episode")
    @Expose
    val episode: JsonArray,
    @SerializedName("url")
    @Expose
    val url: String,
    @SerializedName("created")
    @Expose
    val created: String
)

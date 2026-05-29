package com.example.gamehealthmanager.core.model

import com.google.gson.annotations.SerializedName

data class GameResponse(
    @SerializedName("results") val results: List<Game>
)
data class Game(
    @SerializedName("id") val id: String,
    @SerializedName("title") val titulo: String,
    @SerializedName("genre") val genero: String,
    @SerializedName("platform") val plataforma: String,
    @SerializedName("thumbnail") val imagenUrl: String, // La API real lo llama "thumbnail"
    @SerializedName("release_date") val anioLanzamiento: String, // La API real lo llama "release_date"
    @SerializedName("short_description") val descripcion: String // La API real lo llama "short_description"
)
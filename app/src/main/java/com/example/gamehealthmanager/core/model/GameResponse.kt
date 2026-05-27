package com.example.gamehealthmanager.core.model

import com.google.gson.annotations.SerializedName

data class GameResponse(
    @SerializedName("results") val results: List<Game>
)
data class Game(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val titulo: String,
    @SerializedName("genre") val genero: String,
    @SerializedName("platform") val plataforma: String,
    @SerializedName("image_url") val imagenUrl: String,
    @SerializedName("release_year") val anioLanzamiento: Int,
    @SerializedName("description") val descripcion: String
)
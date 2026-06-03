package com.example.gamehealthmanager.core.model

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// 1. GameResponse no necesita Parcelize porque no lo vas a pasar entre pantallas,
// solo es un contenedor temporal para GSON.
data class GameResponse(
    @SerializedName("results") val results: List<Game>
)

// 2. Game SÍ necesita Parcelize y debe implementar Parcelable
@Parcelize
data class Game(
    @SerializedName("id") val id: Int, // Cambiado a Int para mayor compatibilidad
    @SerializedName("title") val titulo: String,
    @SerializedName("genre") val genero: String,
    @SerializedName("platform") val plataforma: String,
    @SerializedName("thumbnail") val imagenUrl: String,
    @SerializedName("release_date") val anioLanzamiento: String,
    @SerializedName("short_description") val descripcion: String
): Parcelable
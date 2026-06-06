package com.example.gamehealthmanager.core.model

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// 1. Respuesta de la API (RAWG)
data class GameResponse(
    @SerializedName("results") val results: List<Game>
)

// 2. Tu modelo Game ampliado para incluir el estado de salud
@Parcelize
data class Game(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val titulo: String,
    @SerializedName("background_image") val imagenUrl: String?,
    @SerializedName("genres") val generos: List<Genre>?,
    @SerializedName("description_raw") val descripcion: String?,

    // Asigna el valor por defecto aquí mismo:
    var healthRating: HealthRating = HealthRating.NONE
): Parcelable

// 3. Géneros
@Parcelize
data class Genre(
    @SerializedName("name") val name: String
): Parcelable
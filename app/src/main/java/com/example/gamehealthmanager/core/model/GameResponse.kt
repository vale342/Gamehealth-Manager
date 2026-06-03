package com.example.gamehealthmanager.core.model

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// El JSON de RAWG viene así: { "results": [ ... ] }
data class GameResponse(
    @SerializedName("results") val results: List<Game>
)

@Parcelize
data class Game(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val titulo: String, // RAWG usa 'name', no 'title'
    @SerializedName("background_image") val imagenUrl: String?, // RAWG usa 'background_image'
    @SerializedName("genres") val generos: List<Genre>?, // RAWG devuelve una lista de objetos
    @SerializedName("description_raw") val descripcion: String? // RAWG trae esto en el detalle
): Parcelable

// RAWG devuelve los géneros como una lista de objetos
@Parcelize
data class Genre(
    @SerializedName("name") val name: String
): Parcelable
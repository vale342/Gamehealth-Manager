package com.example.gamehealthmanager.core.repositories

import com.example.gamehealthmanager.core.ResponseService
import com.example.gamehealthmanager.core.model.Game
import com.example.gamehealthmanager.core.network.ApiClient
import com.example.gamehealthmanager.core.network.GameService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository : GameService {
    // Recuerda usar .gameApi con la minúscula inicial como lo corregimos en tu ApiClient
    private val api = ApiClient.GameApi

    // 1. Añadimos todos los atributos faltantes en la firma de la función
    override suspend fun getTracks(
        id: String,
        title: String,
        genre: String,
        platform: String,
        imageUrl: String,
        releaseYear: String,
        description: String
    ): ResponseService<List<Game>> =
        withContext(Dispatchers.IO) {
            try {
                // 2. Pasamos exactamente los 7 parámetros que tu GameAPI está esperando
                val response = api.getTracks(
                    id = id,
                    title = title,
                    genre = genre,
                    platform = platform,
                    imageUrl = imageUrl,
                    releaseYear = releaseYear,
                    description = description
                )

                // Evaluamos la respuesta de Retrofit
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        ResponseService.Success(body.results)
                    } else {
                        ResponseService.Error("Respuesta vacía del servidor")
                    }
                } else {
                    ResponseService.Error("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                ResponseService.Error("No se pudieron cargar los juegos: ${e.localizedMessage}")
            }
        }
}
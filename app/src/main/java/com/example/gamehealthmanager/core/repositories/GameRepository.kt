package com.example.gamehealthmanager.core.repositories

import com.example.gamehealthmanager.core.ResponseService
import com.example.gamehealthmanager.core.model.GameResponse
import com.example.gamehealthmanager.core.network.ApiClient
import com.example.gamehealthmanager.core.network.GameService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository : GameService {
    private val api = ApiClient.GameApi

    // Ahora la función solo recibe los filtros, NO todos los campos del juego
    override suspend fun getGames(
        query: String?,
        genre: String?,
        ordering: String
    ): ResponseService<GameResponse> =
        withContext(Dispatchers.IO) {
            try {
                // Llamamos a la API con los filtros de RAWG
                val response = api.getGames(
                    query = query,
                    genre = genre,
                    ordering = ordering
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        ResponseService.Success(body)
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
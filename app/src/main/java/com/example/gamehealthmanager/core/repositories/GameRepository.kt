package com.example.gamehealthmanager.core.repositories

import com.example.gamehealthmanager.core.ResponseService
import com.example.gamehealthmanager.core.model.GameResponse
import com.example.gamehealthmanager.core.network.ApiClient
import com.example.gamehealthmanager.core.network.GameService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository : GameService {
    private val api = ApiClient.GameApi

    // Ahora la función recibe los filtros y los parámetros de paginación
    override suspend fun getGames(
        query: String?,
        genre: String?,
        ordering: String,
        page: Int,        // <-- NUEVO: Recibe la página
        pageSize: Int     // <-- NUEVO: Recibe el tamaño de la página
    ): ResponseService<GameResponse> =
        withContext(Dispatchers.IO) {
            try {
                // Llamamos a la API con todos los parámetros
                val response = api.getGames(
                    query = query,
                    genre = genre,
                    ordering = ordering,
                    page = page,          // <-- NUEVO: Se lo pasa a Retrofit
                    pageSize = pageSize   // <-- NUEVO: Se lo pasa a Retrofit
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
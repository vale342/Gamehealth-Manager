package com.example.gamehealthmanager.core.network

import com.example.gamehealthmanager.core.ResponseService
import com.example.gamehealthmanager.core.model.GameResponse

interface GameService {
    // Solo necesitamos los filtros, no los datos del juego en sí
    suspend fun getGames(
        genre: String? = null,
        ordering: String = "-released"
    ): ResponseService<GameResponse>
}
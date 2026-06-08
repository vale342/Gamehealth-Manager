package com.example.gamehealthmanager.core.network

import com.example.gamehealthmanager.core.ResponseService
import com.example.gamehealthmanager.core.model.GameResponse

interface GameService {
    suspend fun getGames(
        query: String? = null,
        genre: String? = null,
        ordering: String = "-released",
        page: Int = 1,
        pageSize: Int = 20
    ): ResponseService<GameResponse>
}
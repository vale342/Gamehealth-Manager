package com.example.gamehealthmanager.core.network

import com.example.gamehealthmanager.core.model.GameResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GameAPI {
    @GET("games")
    suspend fun getGames(
        @Query("search") query: String? = null, // <--- Esto permite buscar
        @Query("genres") genre: String? = null,
        @Query("ordering") ordering: String = "-released"
    ): Response<GameResponse>
}
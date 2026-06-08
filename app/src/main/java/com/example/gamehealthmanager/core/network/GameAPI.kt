package com.example.gamehealthmanager.core.network

import com.example.gamehealthmanager.core.model.GameResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GameAPI {
    @GET("games")
    suspend fun getGames(
        @Query("search") query: String? = null,
        @Query("genres") genre: String? = null,
        @Query("ordering") ordering: String = "-released",
        @Query("page") page: Int = 1,          // <-- NUEVO: Controla la página actual
        @Query("page_size") pageSize: Int = 20 // <-- NUEVO: Límite de 20 juegos
    ): Response<GameResponse>
}
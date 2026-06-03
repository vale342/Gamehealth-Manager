package com.example.gamehealthmanager.core.network

import com.example.gamehealthmanager.core.model.GameResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GameAPI {
    // Como el interceptor ya pone la ?key=... automáticamente,
    // no necesitamos ponerlo aquí como parámetro.
    @GET("games")
    suspend fun getGames(
        @Query("genres") genre: String? = null,
        @Query("ordering") ordering: String = "-released"
    ): Response<GameResponse>
}
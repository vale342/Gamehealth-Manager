package com.example.gamehealthmanager.core.network

import com.example.gamehealthmanager.core.model.Game
import com.example.gamehealthmanager.core.model.GameResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GameAPI {
    @GET("games")
    suspend fun getGames(
        @Query("platform") platform: String,  // Filtra por: "pc", "browser" o "all"
        @Query("category") genre: String      // Filtra por género: "shooter", "anime", "mmorpg", etc.
    ): Response<List<Game>>
}
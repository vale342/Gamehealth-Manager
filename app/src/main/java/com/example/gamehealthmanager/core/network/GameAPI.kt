package com.example.gamehealthmanager.core.network

import com.example.gamehealthmanager.core.model.GameResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GameAPI {
    @GET("/")
    suspend fun getTracks(
        @Query("id") id: String,
        @Query("title") title: String,
        @Query("genre") genre: String,
        @Query("platform") platform: String,
        @Query("image_url") imageUrl: String,
        @Query("release_year") releaseYear: String, // Cambiado a String
        @Query("description") description: String
    ): Response<GameResponse> // Usamos Response de Retrofit para atrapar .isSuccessful
}
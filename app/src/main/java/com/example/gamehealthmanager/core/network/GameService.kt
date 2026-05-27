package com.example.gamehealthmanager.core.network

import com.example.gamehealthmanager.core.ResponseService
import com.example.gamehealthmanager.core.model.Game

interface GameService {
    suspend fun getTracks(
        id: String,
        title: String,
        genre: String,
        platform: String,
        imageUrl: String,
        releaseYear: String,
        description: String
    ): ResponseService<List<Game>>
}
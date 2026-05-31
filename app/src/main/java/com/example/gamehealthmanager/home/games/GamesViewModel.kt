package com.example.gamehealthmanager.home.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamehealthmanager.core.ResponseService
import com.example.gamehealthmanager.core.model.Game
import com.example.gamehealthmanager.core.network.GameService
import com.example.gamehealthmanager.core.repositories.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GamesViewModel (
    private val service: GameService = GameRepository()
) : ViewModel() {

    private val _gameState = MutableStateFlow<ResponseService<List<Game>>?>(null)
    val gameState: StateFlow<ResponseService<List<Game>>?> = _gameState.asStateFlow()

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _gameState.value = ResponseService.Loading

            // CAMBIO 1: Cambiamos .getGames por .getTracks que es el nombre real en tu repositorio
            // CAMBIO 2: Ponemos "all" en platform para que la API real de Free-To-Play sepa qué buscar
            _gameState.value = service.getGames(
                platform = "pc", // Prueba primero con "pc" para ver si carga
                genre = "",
                // ... otros parámetros
                id = "",
                title = "",
                imageUrl = "",
                releaseYear = "",
                description = ""
            )
        }
    }
}
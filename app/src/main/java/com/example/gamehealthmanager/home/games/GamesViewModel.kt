package com.example.gamehealthmanager.home.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamehealthmanager.core.ResponseService
import com.example.gamehealthmanager.core.model.Game
import com.example.gamehealthmanager.core.model.GameResponse
import com.example.gamehealthmanager.core.network.GameService
import com.example.gamehealthmanager.core.repositories.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GamesViewModel(
    private val service: GameService = GameRepository()
) : ViewModel() {

    // Cambiamos List<Game> por GameResponse
    private val _gameState = MutableStateFlow<ResponseService<GameResponse>?>(null)
    val gameState: StateFlow<ResponseService<GameResponse>?> = _gameState.asStateFlow()

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _gameState.value = ResponseService.Loading

            // Ahora solo pasamos los filtros de RAWG
            _gameState.value = service.getGames(
                query = null,
                genre = null,
                ordering = "-released"
            )
        }
    }

    fun searchGames(query: String) {
        viewModelScope.launch {
            _gameState.value = ResponseService.Loading
            // Llamamos al repositorio pasando el texto de búsqueda
            _gameState.value = service.getGames(query = query)
        }
    }
}
package com.example.gamehealthmanager.home.gameDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamehealthmanager.core.model.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameDetailViewModel : ViewModel() {

    // Estado para saber si el juego es favorito
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    // Estado para cargar los detalles del juego
    private val _gameDetail = MutableStateFlow<Game?>(null)
    val gameDetail: StateFlow<Game?> = _gameDetail.asStateFlow()

    fun loadGameDetail(game: Game) {
        _gameDetail.value = game
        // Aquí podrías verificar en Firestore si el juego ya es favorito
        checkIfFavorite(game.id)
    }

    fun toggleFavorite(game: Game) {
        viewModelScope.launch {
            // Lógica para guardar/quitar en Firestore
            _isFavorite.value = !_isFavorite.value
        }
    }

    private fun checkIfFavorite(gameId: Int) {
        // Aquí consultarías a Firestore:
        // "firestore.collection('favoritos').document(gameId.toString()).get()..."
    }
}
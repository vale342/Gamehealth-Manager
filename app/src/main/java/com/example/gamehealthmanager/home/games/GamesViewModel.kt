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

    private val _gameState = MutableStateFlow<ResponseService<GameResponse>?>(null)
    val gameState: StateFlow<ResponseService<GameResponse>?> = _gameState.asStateFlow()

    // --- VARIABLES DE PAGINACIÓN ---
    private var currentPage = 1
    private var isLoadingPage = false

    // Aquí guardaremos TODOS los juegos que vayamos descargando
    private val accumulatedGames = mutableListOf<Game>()

    init {
        loadGames()
    }

    fun loadGames() {
        // Evitar dobles llamadas si ya está descargando
        if (isLoadingPage) return
        isLoadingPage = true

        viewModelScope.launch {
            // Solo mostramos el "loader" general en la primera página
            if (currentPage == 1) {
                _gameState.value = ResponseService.Loading
            }

            try {
                // Pedimos la página actual a tu servicio
                val response = service.getGames(
                    query = null,
                    genre = null,
                    ordering = "-released",
                    page = currentPage,     // <-- Pasamos la página
                    pageSize = 20           // <-- Límite de 20
                )

                if (response is ResponseService.Success) {
                    // Extraemos los juegos nuevos (Asumo que tu lista en GameResponse se llama 'results')
                    val newGames = response.data.results

                    // Sumamos los nuevos a los que ya teníamos guardados
                    accumulatedGames.addAll(newGames)

                    // Creamos una copia de la respuesta, pero metiéndole TODOS los juegos acumulados
                    val combinedResponse = GameResponse(
                        count = response.data.count,
                        next = response.data.next,
                        previous = response.data.previous,
                        results = accumulatedGames
                    )

                    // Emitimos el éxito para que el Fragmento lo dibuje
                    _gameState.value = ResponseService.Success(combinedResponse)

                    // Preparamos el número para la siguiente vez que el usuario baje
                    currentPage++
                } else if (response is ResponseService.Error) {
                    if (currentPage == 1) _gameState.value = response
                }

            } catch (e: Exception) {
                if (currentPage == 1) _gameState.value = ResponseService.Error(e.message ?: "Error desconocido")
            } finally {
                // Liberamos el candado
                isLoadingPage = false
            }
        }
    }

    fun searchGames(query: String) {
        viewModelScope.launch {
            _gameState.value = ResponseService.Loading

            // Si buscamos algo nuevo, reiniciamos la paginación a 1 y limpiamos la lista
            currentPage = 1
            accumulatedGames.clear()

            // AQUÍ ESTABA EL DETALLE: Cambiamos el 30 por el 20
            val response = service.getGames(query = query, page = 1, pageSize = 20)

            if (response is ResponseService.Success) {
                accumulatedGames.addAll(response.data.results)

                val combinedResponse = GameResponse(
                    count = response.data.count,
                    next = response.data.next,
                    previous = response.data.previous,
                    results = accumulatedGames
                )
                _gameState.value = ResponseService.Success(combinedResponse)
            } else {
                _gameState.value = response
            }
        }
    }
}
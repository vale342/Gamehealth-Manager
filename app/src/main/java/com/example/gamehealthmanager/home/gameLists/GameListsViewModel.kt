package com.example.gamehealthmanager.home.gameLists

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameListsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _favoriteGames = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val favoriteGames = _favoriteGames.asStateFlow()

    private val _allGames = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val allGames = _allGames.asStateFlow()

    fun loadAllData() {
        // Centralizamos el inicio: llamamos a juegos primero.
        // loadFavorites() se llamará automáticamente cuando termine.
        loadAllGames()
    }

    fun loadAllGames() {
        val userId = auth.currentUser?.uid ?: return

        // 1. Buscamos en la nueva ruta: subcolección 'ratings' del usuario
        db.collection("users").document(userId).collection("ratings").get()
            .addOnSuccessListener { ratingsSnapshot ->

                val gamesList = ratingsSnapshot.documents.map { doc ->
                    val data = doc.data?.toMutableMap() ?: mutableMapOf()
                    data["id"] = doc.id // El nombre del documento es el ID del juego
                    data
                }

                _allGames.value = gamesList

                // 2. ¡Clave del éxito! Cargamos favoritos solo cuando allGames ya tiene datos
                loadFavorites()
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE", "Error al cargar juegos: ${e.message}")
            }
    }

    fun loadFavorites() {
        val userId = auth.currentUser?.uid ?: return

        // 3. Buscamos en la nueva ruta: subcolección 'favorites' del usuario
        db.collection("users").document(userId).collection("favorites").get()
            .addOnSuccessListener { favSnapshot ->

                // Extraemos los IDs de los juegos favoritos directamente del nombre del documento
                val favIds = favSnapshot.documents.map { it.id }

                // Filtramos la lista completa que ya cargamos en el paso 1
                _favoriteGames.value = _allGames.value.filter { game ->
                    favIds.contains(game["id"]?.toString())
                }
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE", "Error al cargar favoritos: ${e.message}")
            }
    }
}
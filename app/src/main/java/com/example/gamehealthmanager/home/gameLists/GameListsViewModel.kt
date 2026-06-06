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
        // Ahora podemos cargar ambas listas al mismo tiempo porque son independientes
        loadAllGames()
        loadFavorites()
    }

    fun loadAllGames() {
        val userId = auth.currentUser?.uid ?: return

        // 1. Cargamos los juegos calificados (Semáforo)
        db.collection("users").document(userId).collection("ratings").get()
            .addOnSuccessListener { ratingsSnapshot ->
                val gamesList = ratingsSnapshot.documents.map { doc ->
                    val data = doc.data?.toMutableMap() ?: mutableMapOf()
                    data["id"] = doc.id
                    data
                }
                _allGames.value = gamesList
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE", "Error al cargar juegos: ${e.message}")
            }
    }

    fun loadFavorites() {
        val userId = auth.currentUser?.uid ?: return

        // 2. ¡EL ARREGLO! Leemos los favoritos DIRECTAMENTE de su propia base de datos
        // Ya no filtramos _allGames, usamos los datos completos que tú misma guardaste.
        db.collection("users").document(userId).collection("favorites").get()
            .addOnSuccessListener { favSnapshot ->
                val favList = favSnapshot.documents.map { doc ->
                    val data = doc.data?.toMutableMap() ?: mutableMapOf()
                    data["id"] = doc.id
                    data
                }
                _favoriteGames.value = favList
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE", "Error al cargar favoritos: ${e.message}")
            }
    }
}
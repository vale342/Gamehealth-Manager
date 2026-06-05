package com.example.gamehealthmanager.home.gameDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamehealthmanager.core.model.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameDetailViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val _gameDetail = MutableStateFlow<Game?>(null)
    val gameDetail: StateFlow<Game?> = _gameDetail.asStateFlow()

    // Lógica nueva de Firestore
    // En tu GameDetailViewModel.kt
    fun saveRating(gameId: String, title: String, rating: String, imageUrl: String?, genre: String?, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val ratingData = hashMapOf(
            "rating" to rating,
            "titulo" to title, // Usamos 'titulo' para que coincida con tu adaptador
            "imagen" to imageUrl,
            "genero" to genre,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("users").document(userId).collection("ratings")
            .document(gameId)
            .set(ratingData)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun loadRating(gameId: String, onResult: (String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).collection("ratings")
            .document(gameId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) onResult(document.getString("rating"))
                else onResult(null)
            }
    }

    fun toggleFavorite(gameId: String) {
        val userId = auth.currentUser?.uid ?: return

        // NUEVA RUTA: users -> userId -> favorites -> gameId
        val favRef = db.collection("users").document(userId).collection("favorites").document(gameId)

        favRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                favRef.delete().addOnSuccessListener { _isFavorite.value = false }
            } else {
                // Solo guardamos la fecha o un campo simple, el ID del documento ya es el gameId
                val favData = hashMapOf("timestamp" to FieldValue.serverTimestamp())
                favRef.set(favData).addOnSuccessListener { _isFavorite.value = true }
            }
        }.addOnFailureListener {
            // Manejo de error opcional
        }
    }

    fun checkFavoriteStatus(gameId: String) {
        val userId = auth.currentUser?.uid ?: return

        // NUEVA RUTA para verificar
        db.collection("users").document(userId).collection("favorites").document(gameId)
            .get()
            .addOnSuccessListener { _isFavorite.value = it.exists() }
    }
}
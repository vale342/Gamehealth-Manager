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
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _gameDetail = MutableStateFlow<Game?>(null)
    val gameDetail: StateFlow<Game?> = _gameDetail.asStateFlow()

    // Lógica nueva de Firestore
    fun saveRating(gameId: String, title: String, rating: String, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val ratingData = hashMapOf(
            "rating" to rating,
            "gameName" to title,
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

    fun toggleFavorite(game: Game) {
        viewModelScope.launch { _isFavorite.value = !_isFavorite.value }
    }
}
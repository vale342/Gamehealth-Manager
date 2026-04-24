package com.example.gamehealthmanager.core

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(): Authentication {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun requestLogin(
        email: String,
        password: String
    ): FirebaseUser? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en login: ${e.message}", e)
            null
        }
    }

    override suspend fun requestSignUp(
        email: String,
        password: String
    ): FirebaseUser? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en registro: ${e.message}", e)
            null
        }
    }
}
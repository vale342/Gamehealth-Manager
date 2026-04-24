package com.example.gamehealthmanager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamehealthmanager.core.AuthRepository
import kotlinx.coroutines.launch


//Con esta implementación hace que permite encontrar el viewModel en otro fragmento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignInViewModel: ViewModel() {
    val repository = AuthRepository()

    private val _signUpSuccess = MutableStateFlow<Boolean?>(null)
    val signUpSuccess: StateFlow<Boolean?> = _signUpSuccess

    fun requestSignUp(email: String, password: String) {
        Log.d("VALE_DEBUG", "Iniciando requestSignUp para: $email")
        viewModelScope.launch {
            try {
                val result = repository.requestSignUp(email, password)
                if (result != null) {
                    Log.i("VALE_DEBUG", "¡ÉXITO! Usuario creado con UID: ${result.uid}")
                    _signUpSuccess.value = true
                } else {
                    Log.e("VALE_DEBUG", "ERROR: El repositorio devolvió NULL (posible fallo de Firebase)")
                    _signUpSuccess.value = false
                }
            } catch (e: Exception) {
                Log.e("VALE_DEBUG", "FALLO CRÍTICO: ${e.message}", e)
                _signUpSuccess.value = false
            }
        }
    }

    fun resetState() {
        _signUpSuccess.value = null
    }
}
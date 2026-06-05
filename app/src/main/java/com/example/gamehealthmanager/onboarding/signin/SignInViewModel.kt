package com.example.gamehealthmanager.onboarding.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamehealthmanager.core.AuthRepository
import com.example.gamehealthmanager.core.ResponseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {
    val repository = AuthRepository()
    private val _signInState = MutableStateFlow<ResponseService<FirebaseUser>?>(null)
    val signInState: StateFlow<ResponseService<FirebaseUser>?> = _signInState.asStateFlow()
    private val _resetState = MutableStateFlow<ResponseService<Boolean>?>(null)
    val resetState: StateFlow<ResponseService<Boolean>?> = _resetState.asStateFlow()

    // Validaciones
    fun validateEmail(email: String): String? {
        if (email.isBlank()) return "El correo es requerido"
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "Correo inválido"
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isBlank()) return "La contraseña es requerida"
        if (password.length < 8) return "Mínimo 8 caracteres"
        return null
    }

    fun isLoginFormValid(email: String, password: String): Boolean {
        return validateEmail(email) == null &&
                validatePassword(password) == null
    }

    // --- Operación de login ---
    fun requestLogin(email: String, password: String) {
        viewModelScope.launch {
            _signInState.value = ResponseService.Loading
            _signInState.value = repository.requestLogin(email, password)
        }
    }

    // --- NUEVO: Operación de recuperación de contraseña ---
    fun requestPasswordReset(email: String) {
        // Validamos el correo antes de enviar la petición a Firebase
        val emailError = validateEmail(email)
        if (emailError != null) {
            _resetState.value = ResponseService.Error("Ingresa un correo válido para recuperar tu contraseña")
            return
        }

        _resetState.value = ResponseService.Loading

        // Petición a Firebase
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _resetState.value = ResponseService.Success(true)
                } else {
                    _resetState.value = ResponseService.Error(task.exception?.message ?: "Error al enviar el correo")
                }
            }
    }
}
package com.adrianserranoquero.authapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _authState.value = if (task.isSuccessful) {
                        AuthState.Success(auth.currentUser?.email ?: "Usuario registrado")
                    } else {
                        AuthState.Error(task.exception?.message ?: "Error al registrar")
                    }
                }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _authState.value = if (task.isSuccessful) {
                        AuthState.Success(auth.currentUser?.email ?: "Usuario autenticado")
                    } else {
                        AuthState.Error(task.exception?.message ?: "Error al iniciar sesión")
                    }
                }
        }
    }

    fun loginAnonymously() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    _authState.value = if (task.isSuccessful) {
                        AuthState.Success("Usuario anónimo autenticado")
                    } else {
                        AuthState.Error(task.exception?.message ?: "Error en autenticación anónima")
                    }
                }
        }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val error: String) : AuthState()
}

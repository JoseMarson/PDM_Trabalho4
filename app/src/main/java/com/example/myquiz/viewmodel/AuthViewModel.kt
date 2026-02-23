package com.example.myquiz.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myquiz.data.model.User
import com.example.myquiz.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        repository.login(email, password, onSuccess, onError)
    }

    fun register(
        nome: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        repository.register( nome , email, password, onSuccess, onError)
    }

    fun logout() {
        repository.logout()
    }

    fun isUserLogged(): Boolean {
        return repository.isUserLogged()
    }

    var user by mutableStateOf<User?>(null)
        private set

    fun startListeningUser() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        repository.listenUserData(uid) {
            user = it
        }
    }

}
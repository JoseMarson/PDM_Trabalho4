// ui/feature/home/HomeViewModel.kt
package com.eduardoomarson.quizpdm.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardoomarson.quizpdm.data.local.dao.UserDao
import com.eduardoomarson.quizpdm.data.repository.QuizRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: QuizRepository,
    private val userDao: UserDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Pega o usuário logado direto do Firebase Auth
    private val currentUser = FirebaseAuth.getInstance().currentUser

    init {
        loadUserAndSync()
    }

    private fun loadUserAndSync() {
        val userId = currentUser?.uid ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Carrega do Room imediatamente (offline primeiramente)
            val localUser = userDao.getUserById(userId)
            if (localUser != null) {
                _uiState.update {
                    it.copy(
                        userName = localUser.name,
                        userPic = localUser.pic,
                        userScore = localUser.totalScore
                    )
                }
            } else {
                // Usuário novo (Google) ou sem perfil ainda, sendo assim usa ProfileSetup
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        needsProfileSetup = true    // Atualiza parametro do HomeUiState
                    )
                }
                return@launch                       // necessário que pare aqui, não sincroniza
            }

            // 2. Sincroniza com a nuvem em background (irá ser realizada se user já tem perfil)
            try {
                repository.syncOnLogin(userId)
                val updatedUser = userDao.getUserById(userId)
                if (updatedUser != null) {
                    _uiState.update {
                        it.copy(
                            userName = updatedUser.name,
                            userPic = updatedUser.pic,
                            userScore = updatedUser.totalScore,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnLogoutClick -> {
                FirebaseAuth.getInstance().signOut()
            }
            else -> { /* navegação tratada na HomeScreen */ }
        }
    }
}
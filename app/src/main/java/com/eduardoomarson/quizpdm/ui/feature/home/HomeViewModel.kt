package com.eduardoomarson.quizpdm.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardoomarson.quizpdm.data.local.dao.UserDao
import com.eduardoomarson.quizpdm.data.remote.firestore.FirestoreRepository
import com.eduardoomarson.quizpdm.data.repository.QuizRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
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
    private val userDao: UserDao,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Pega o usuário logado direto do Firebase Auth
    private val currentUser = FirebaseAuth.getInstance().currentUser

    // Para atualizações do banco de dados de quizzes:
    private var quizListener: ListenerRegistration? = null

    init {
        loadUserAndSync()
        observeUserScore() // Para atualizar pontuação do usuário - Sugestão CLAUDE
        syncQuizzesInBackground() // Para atualizar banco de Quizzes sempre que entrar em Home
    }

    private fun loadUserAndSync() {
        val userId = currentUser?.uid ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val localUser = userDao.getUserById(userId)
            /* LLM: CLAUDE
               PROMPT: ao logar no mesmo usuário do computador no celular, o score e o avatar do
               usuário já cadastrado antes no computador nao apareceu no celular, ou seja, ele
               nao puxou da nuvem os dados do usuário, quais alterações devem ser realizadas para
                que isso não ocorra?
            */

            // Inicio Sugestão CLAUDE
            if (localUser == null) {
                // Dispositivo novo ou usuário Google sem perfil ainda
                // Tenta buscar da nuvem antes de decidir
                try {
                    repository.syncOnLogin(userId)
                    val cloudUser = userDao.getUserById(userId)

                    if (cloudUser == null) {
                        // Realmente é novo (email/Google sem perfil criado ainda)
                        _uiState.update {
                            it.copy(isLoading = false, needsProfileSetup = true)
                        }
                        return@launch
                    }
                    // Fim Sugestão CLAUDE

                    // Tinha perfil na nuvem (ex: usuário Google em dispositivo novo)
                    _uiState.update {
                        it.copy(
                            userName = cloudUser.name,
                            userPic = cloudUser.pic,
                            userScore = cloudUser.totalScore,
                            isLoading = false
                        )
                    }

                } catch (e: Exception) {
                    // Sem internet e sem dados locais → pede ProfileSetup
                    _uiState.update {
                        it.copy(isLoading = false, needsProfileSetup = true)
                    }
                }
                return@launch
            }

            // Tem dados locais → carrega imediatamente (offline first)
            _uiState.update {
                it.copy(
                    userName = localUser.name,
                    userPic = localUser.pic,
                    userScore = localUser.totalScore
                )
            }

            // Sincroniza com nuvem em background
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

    // LLM: CLAUDE
    // PROMPT: Finalizei o quiz mas o score não foi atualizado na HomeScreen, quais mudanças devem ser realizadas?
    // INICIO SUGESTÃO CLAUDE
    private fun observeUserScore() {
        val userId = currentUser?.uid ?: return
        viewModelScope.launch {
            userDao.observeUser(userId).collect { user ->
                if (user != null) {
                    _uiState.update {
                        it.copy(
                            userName = user.name,
                            userPic = user.pic,
                            userScore = user.totalScore
                        )
                    }
                }
            }
        }
    }
    // FIM SUGESTÃO CLAUDE - há mudanças em UserDao e QuizRepository também decorrentes dessa dúvida.

    private fun syncQuizzesInBackground() {
        quizListener = firestoreRepo.observeQuizzes { remoteQuizzes ->
            viewModelScope.launch {
                repository.syncQuizzesAndQuestions(remoteQuizzes)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        quizListener?.remove()   // cancela o listener ao sair
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
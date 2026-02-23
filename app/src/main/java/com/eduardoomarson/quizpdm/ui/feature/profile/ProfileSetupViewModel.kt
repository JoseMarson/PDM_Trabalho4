package com.eduardoomarson.quizpdm.ui.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardoomarson.quizpdm.data.local.dao.UserDao
import com.eduardoomarson.quizpdm.data.local.entities.UserEntity
import com.eduardoomarson.quizpdm.data.remote.firestore.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val userDao: UserDao,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSetupUiState())
    val uiState: StateFlow<ProfileSetupUiState> = _uiState.asStateFlow()

    private val currentUser = FirebaseAuth.getInstance().currentUser

    /*  LLM: CLAUDE
        PROMPT: Gostaria que o avatar e apelido do usuário já mostrasse no ProfileSetupScreen
               caso já exista.
     */
    // Início sugestão Claude
    init {
        loadCurrentProfile()
    }

    private fun loadCurrentProfile() {
        val userId = currentUser?.uid ?: return
        viewModelScope.launch {
            val existingUser = userDao.getUserById(userId) ?: return@launch

            // Converte "person1" de volta para o resource ID
            val avatarResId = availableAvatars.getOrNull(
                existingUser.pic
                    .removePrefix("person")
                    .toIntOrNull()
                    ?.minus(1) ?: -1
            )

            _uiState.update {
                it.copy(
                    name = existingUser.name,
                    selectedAvatarRes = avatarResId
                )
            }
        }
    }
    // Fim sugestão CLAUDE

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onAvatarSelected(avatarRes: Int) {
        _uiState.update { it.copy(selectedAvatarRes = avatarRes) }
    }

    fun saveProfile() {
        val userId = currentUser?.uid ?: return
        val state = _uiState.value

        if (state.name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Digite seu nome") }
            return
        }
        if (state.selectedAvatarRes == null) {
            _uiState.update { it.copy(errorMessage = "Escolha um avatar") }
            return
        }

        // Salva o nome do recurso como String (ex: "person1")
        val picName = avatarResIdToName(state.selectedAvatarRes)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Busca o usuário atual para preservar o totalScore
                val existingUser = userDao.getUserById(userId)

                val user = UserEntity(
                    id = userId,
                    name = state.name,
                    pic = picName,
                    totalScore = existingUser?.totalScore ?: 0,  // Sugestão CLAUDE para preservar o score
                    lastSyncAt = System.currentTimeMillis()
                )
                userDao.upsertUser(user)        // salva local
                firestoreRepo.saveUser(user)    // salva na nuvem
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Erro ao salvar: ${e.message}")
                }
            }
        }
    }

    // Converte o resourceId de volta para o nome do drawable
    private fun avatarResIdToName(resId: Int): String {
        return availableAvatars.indexOf(resId)
            .takeIf { it >= 0 }
            ?.let { "person${it + 1}" }
            ?: "person1"
    }
}
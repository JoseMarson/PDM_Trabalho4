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
                val user = UserEntity(
                    id = userId,
                    name = state.name,
                    pic = picName,
                    totalScore = 0,
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
package com.eduardoomarson.quizpdm.ui.feature.profile

data class ProfileSetupUiState(
    val name: String = "",
    val selectedAvatarRes: Int? = null,  // Resource ID do drawable
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

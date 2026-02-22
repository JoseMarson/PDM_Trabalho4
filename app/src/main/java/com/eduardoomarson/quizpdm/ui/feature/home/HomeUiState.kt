package com.eduardoomarson.quizpdm.ui.feature.home

data class HomeUiState(
    val userName: String = "",
    val userPic: String = "",
    val userScore: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

package com.eduardoomarson.quizpdm.ui.feature.quizlist

data class QuizListUiState(
    val quizzes: List<QuizModel> = emptyList(),
    val category: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

package com.eduardoomarson.quizpdm.ui.feature.createquiz

data class CreateQuizUiState(
    val title: String = "",
    val description: String = "",
    val selectedCategory: String = "",
    val difficulty: String = "médio",
    val questions: List<QuestionInput> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

// Categorias disponíveis
val availableCategories = listOf(
    "Ciências",
    "História",
    "Esportes",
    "Geral",
    "Tecnologia",
    "Direito",
    "Inglês",
    "Japonês"
)

val availableDifficulties = listOf("fácil", "médio", "difícil")
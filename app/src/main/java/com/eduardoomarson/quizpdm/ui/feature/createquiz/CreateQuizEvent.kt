package com.eduardoomarson.quizpdm.ui.feature.createquiz

sealed interface CreateQuizEvent {
    data class OnTitleChange(val title: String) : CreateQuizEvent
    data class OnDescriptionChange(val description: String) : CreateQuizEvent
    data class OnCategoryChange(val category: String) : CreateQuizEvent
    data class OnDifficultyChange(val difficulty: String) : CreateQuizEvent
    data class OnAddQuestion(val question: QuestionInput) : CreateQuizEvent
    data class OnRemoveQuestion(val index: Int) : CreateQuizEvent
    data object OnSaveQuiz : CreateQuizEvent
}

data class QuestionInput(
    val question: String = "",
    val answer1: String = "",
    val answer2: String = "",
    val answer3: String = "",
    val answer4: String = "",
    val correctAnswer: String = "a",
    val picPath: String = ""
)
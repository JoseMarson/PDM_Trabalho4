package com.eduardoomarson.quizpdm.ui.feature.quiz

import com.eduardoomarson.quizpdm.ui.feature.questions.QuestionModel

data class QuizUiState(
    val questions: List<QuestionModel> = emptyList(),
    val quizId: String = "",
    val quizCategory: String = "",
    val currentIndex: Int = 0,
    val score: Int = 0,
    val correctAnswers: Int = 0,
    val totalQuestions: Int = 0,
    val timeSeconds: Long = 0L,
    val maxScore: Int = 0,
    val isLoading: Boolean = false,
    val isFinished: Boolean = false,
    val errorMessage: String? = null
)
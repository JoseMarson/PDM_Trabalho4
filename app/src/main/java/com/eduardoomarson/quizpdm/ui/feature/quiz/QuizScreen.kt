package com.eduardoomarson.quizpdm.ui.feature.quiz

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eduardoomarson.quizpdm.ui.feature.questions.QuestionScreen
import com.eduardoomarson.quizpdm.ui.feature.score.ScoreScreen

@Composable
fun QuizScreen(
    category: String? = null,
    quizId: String? = null,
    onBackClick: () -> Unit,
    onFinish: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Carrega ao entrar na tela
    LaunchedEffect(Unit) {
        when {
            quizId != null   -> viewModel.loadQuizById(quizId)
            category != null -> viewModel.loadQuizByCategory(category)
            else             -> viewModel.loadRandomQuiz()
        }
    }

    when {
        uiState.isLoading -> {
            QuizLoadingScreen()
        }
        uiState.errorMessage != null -> {
            QuizErrorScreen(
                message = uiState.errorMessage!!,
                onBackClick = onBackClick
            )
        }

        uiState.isFinished -> ScoreScreen(
            score = uiState.score,
            totalQuestions = uiState.totalQuestions,
            correctAnswers = uiState.correctAnswers,
            timeSeconds = uiState.timeSeconds,
            maxScore = uiState.maxScore,
            onBackToMain = onFinish
        )

        uiState.questions.isNotEmpty() -> {
            QuestionScreen(
                questions = uiState.questions,
                category = uiState.quizCategory,
                onFinish = { finalScore, answeredQuestions ->
                    viewModel.onQuizFinished(finalScore, answeredQuestions)
                },
                onBackClick = onBackClick
            )
        }
    }
}


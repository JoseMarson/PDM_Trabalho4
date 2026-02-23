package com.eduardoomarson.quizpdm.ui.feature.quiz

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eduardoomarson.quizpdm.ui.feature.questions.QuestionScreen

@Composable
fun QuizScreen(
    category: String? = null,
    onBackClick: () -> Unit,
    onFinish: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Carrega ao entrar na tela
    LaunchedEffect(Unit) {
        if (category != null) {
            viewModel.loadQuizByCategory(category)
        } else {
            viewModel.loadRandomQuiz()
        }
    }

    // Navega ao finalizar
    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) onFinish()
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

        uiState.questions.isNotEmpty() -> {
            QuestionScreen(
                questions = uiState.questions,
                onFinish = { viewModel.onQuizFinished() },
                onBackClick = onBackClick
            )
        }
    }
}


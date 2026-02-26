package com.eduardoomarson.quizpdm.ui.feature.quizlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eduardoomarson.quizpdm.R
import com.eduardoomarson.quizpdm.ui.feature.quizlist.components.QuizCard

@Composable
fun QuizListScreen(
    category: String,
    onQuizClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: QuizListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(category) {
        viewModel.loadQuizzesByCategory(category)
    }

    QuizListContent(
        uiState = uiState,
        onQuizClick = onQuizClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizListContent(
    uiState: QuizListUiState,
    onQuizClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.category,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.orange)
                    )
                }
            }

            uiState.quizzes.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "😕", fontSize = 48.sp)
                        Text(
                            text = "Nenhum quiz nessa categoria",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.navy_blue)
                        )
                        Text(
                            text = "Seja o primeiro a criar um!",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(colorResource(R.color.grey))
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(uiState.quizzes) { quiz ->
                        QuizCard(
                            quiz = quiz,
                            onClick = { onQuizClick(quiz.id) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun QuizListScreenPreview() {
    QuizListContent(
        uiState = QuizListUiState(
            category = "Japonês",
            quizzes = listOf(
                QuizModel(
                    id = "1",
                    title = "Quiz de Japonês Básico",
                    description = "Aprenda hiragana e katakana",
                    category = "Japonês",
                    difficulty = "fácil",
                    totalScore = 30
                ),
                QuizModel(
                    id = "2",
                    title = "Kanji Intermediário",
                    description = "Quiz de kanji nível N3",
                    category = "Japonês",
                    difficulty = "difícil",
                    totalScore = 100
                )
            )
        ),
        onQuizClick = {},
        onBackClick = {}
    )
}
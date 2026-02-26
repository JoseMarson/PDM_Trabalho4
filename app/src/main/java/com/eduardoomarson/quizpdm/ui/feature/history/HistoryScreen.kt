package com.eduardoomarson.quizpdm.ui.feature.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eduardoomarson.quizpdm.R
import com.eduardoomarson.quizpdm.data.local.entities.HistoryEntity
import com.eduardoomarson.quizpdm.ui.feature.home.components.BottomNavigationBar
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onBoardClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onBoardClick = onBoardClick,
        onHistoryClick = onHistoryClick,
        onProfileClick = onProfileClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenContent(
    uiState: HistoryUiState,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onBoardClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico") },
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = colorResource(id = R.color.grey))
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.history.isEmpty() -> {
                    EmptyHistoryState(modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.history, key = { it.id }) { item ->
                            HistoryCard(item = item)
                        }
                    }
                }
            }

            BottomNavigationBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                onItemSelected = { itemId ->
                    when (itemId) {
                        R.id.home -> onHomeClick()
                        R.id.board -> onBoardClick()
                        R.id.profile -> onProfileClick()
                        R.id.history -> onHistoryClick()
                    }
                }
            )
        }
    }
}

@Composable
fun HistoryCard(item: HistoryEntity) {
    val percentage = if (item.totalQuestions > 0)
        (item.correctAnswers * 100) / item.totalQuestions else 0

    val scoreColor = when {
        percentage >= 80 -> Color(0xFF4CAF50)
        percentage >= 50 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.quizTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatDate(item.playedAt),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEEEEEE))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(text = item.category, fontSize = 11.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatChip(
                    icon = {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = scoreColor,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = "${item.correctAnswers}/${item.totalQuestions} acertos"
                )
                StatChip(
                    icon = {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = "${item.score} pts"
                )
                StatChip(
                    icon = {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = formatTime(item.timeSeconds)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { percentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = scoreColor,
                trackColor = Color(0xFFEEEEEE)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$percentage% de aproveitamento",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun StatChip(icon: @Composable () -> Unit, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 13.sp, color = Color.DarkGray)
    }
}

@Composable
fun EmptyHistoryState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "📋", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Nenhum jogo ainda", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Seus jogos aparecerão aqui após a primeira partida.",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

private fun formatDate(timestamp: Long): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))

private fun formatTime(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return if (m > 0) "${m}m ${s}s" else "${s}s"
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    val fakeHistory = listOf(
        HistoryEntity(
            id = "1", userId = "u1", quizId = "q1",
            quizTitle = "Quiz de Ciências", category = "Ciências",
            totalQuestions = 10, correctAnswers = 8, score = 800, maxScore = 1000,
            timeSeconds = 95L, playedAt = System.currentTimeMillis()
        ),
        HistoryEntity(
            id = "2", userId = "u1", quizId = "q2",
            quizTitle = "Desafio de História do Brasil", category = "História",
            totalQuestions = 10, correctAnswers = 4, score = 400, maxScore = 1000,
            timeSeconds = 210L, playedAt = System.currentTimeMillis() - 86400000
        ),
        HistoryEntity(
            id = "3", userId = "u1", quizId = "q3",
            quizTitle = "Matemática Básica", category = "Matemática",
            totalQuestions = 5, correctAnswers = 5, score = 500, maxScore = 500,
            timeSeconds = 60L, playedAt = System.currentTimeMillis() - 172800000
        )
    )
    HistoryScreenContent(uiState = HistoryUiState(history = fakeHistory))
}
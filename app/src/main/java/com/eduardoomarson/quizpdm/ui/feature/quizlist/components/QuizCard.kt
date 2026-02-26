package com.eduardoomarson.quizpdm.ui.feature.quizlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eduardoomarson.quizpdm.R
import com.eduardoomarson.quizpdm.ui.feature.quizlist.QuizModel

@Composable
fun QuizCard(
    quiz: QuizModel,
    onClick: () -> Unit
) {
    val difficultyColor = when (quiz.difficulty) {
        "fácil" -> Color(0xFF4CAF50)
        "médio" -> Color(0xFFFF9800)
        "difícil" -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Título e dificuldade
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = quiz.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.navy_blue),
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(difficultyColor.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = quiz.difficulty,
                    fontSize = 12.sp,
                    color = difficultyColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Descrição
        if (quiz.description.isNotBlank()) {
            Text(
                text = quiz.description,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2
            )
        }

        // Pontuação máxima
        Text(
            text = "🏆 Pontuação máxima: ${quiz.totalScore} pts",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
fun QuizCardPreview() {
    QuizCard(
        quiz = QuizModel(
            id = "1",
            title = "Quiz de Japonês Básico",
            description = "Aprenda hiragana e katakana",
            category = "Japonês",
            difficulty = "fácil",
            totalScore = 30
        ),
        onClick = {}
    )
}
package com.eduardoomarson.quizpdm.ui.feature.quizlist

import com.eduardoomarson.quizpdm.data.local.entities.QuizEntity

data class QuizModel(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val totalScore: Int
)

// Função de conversão
fun QuizEntity.toModel() = QuizModel(
    id = id,
    title = title,
    description = description,
    category = category,
    difficulty = difficulty,
    totalScore = totalScore
)

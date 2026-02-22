package com.eduardoomarson.quizpdm.data.local.entities

import androidx.room.Entity

// Tabela que salva o progresso do usuário em cada quiz
@Entity(
    tableName = "user_quiz_progress",
    primaryKeys = ["userId", "quizId"]
)
data class UserQuizProgressEntity(
    val userId: String,
    val quizId: Int,
    val score: Int,
    val completed: Boolean,
    val completedAt: Long = 0L
)
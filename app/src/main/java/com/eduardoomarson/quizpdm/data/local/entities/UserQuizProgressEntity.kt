package com.eduardoomarson.quizpdm.data.local.entities

import androidx.room.Entity

// Tabela que salva o progresso do usuário em cada quiz
@Entity(
    tableName = "user_quiz_progress",
    primaryKeys = ["userId", "quizId"]
)
data class UserQuizProgressEntity(
    val userId: String = "",
    val quizId: String = "",
    val score: Int = 0,
    val completed: Boolean = false,
    val completedAt: Long = 0L
)
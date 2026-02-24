package com.eduardoomarson.quizpdm.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_history")
data class HistoryEntity(
    @PrimaryKey val id: String = "",
    val userId: String = "",
    val quizId: String = "",
    val quizTitle: String  = "",
    val category: String = "",
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val score: Int = 0,
    val maxScore: Int = 0,
    val timeSeconds: Long = 0L,
    val playedAt: Long = System.currentTimeMillis()
)

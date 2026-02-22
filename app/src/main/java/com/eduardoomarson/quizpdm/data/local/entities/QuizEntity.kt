package com.eduardoomarson.quizpdm.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quizzes")
data class QuizEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val imageUrl: String,
    val questionIds: String, // JSON serializado: "[1,2,3]"
    val totalScore: Int,
    val createdAt: Long
)
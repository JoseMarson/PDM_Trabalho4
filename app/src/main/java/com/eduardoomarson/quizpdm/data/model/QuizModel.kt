package com.eduardoomarson.quizpdm.data.model

data class QuizModel(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val difficulty: String = "", // "easy", "medium", "hard"
    val imageUrl: String = "",
    val questionIds: List<Int> = emptyList(),
    val totalScore: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

package com.eduardoomarson.quizpdm.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String = "",
    val quizId: String = "",         // FK para o Quiz
    val question: String = "",
    val answer1: String = "",
    val answer2: String = "",
    val answer3: String = "",
    val answer4: String = "",
    val correctAnswer: String = "",
    val score: Int = 0,
    val picPath: String = ""
)
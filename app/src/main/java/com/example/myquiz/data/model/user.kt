package com.example.myquiz.data.model

data class User(
    val uid: String = "",
    val nome : String = "",
    val pontos : Int = 0,
    val posicao : Int = 0,
    val quizFeitos : Int = 0
)
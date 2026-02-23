package com.example.myquiz.data.model

data class Question(
    val pergunta: String,
    val A: String,
    val B: String,
    val C: String,
    val D: String,
    val respostaCorreta: String,
    val nomeQuiz: String,

)
package com.example.myquiz.data.model
import com.google.firebase.Timestamp
data class Quiz(
    val id: String = "",
    val nome: String = "",
    val criadorId: String = "",
    val criadoEm: Long = Timestamp.now().seconds
)



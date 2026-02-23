package com.eduardoomarson.quizpdm.ui.feature.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardoomarson.quizpdm.data.local.entities.UserQuizProgressEntity
import com.eduardoomarson.quizpdm.data.repository.QuizRepository
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val repository: QuizRepository,
    private val userId: String
) : ViewModel() {

    // Chamado ao fazer login
    fun onUserLogin() {
        viewModelScope.launch {
            try {
                repository.syncOnLogin(userId)
            } catch (e: Exception) {
                // tratar erro de conexão — dados locais ainda funcionam offline
            }
        }
    }

    // Chamado ao finalizar um quiz
    fun onQuizFinished(quizId: Int, finalScore: Int) {
        viewModelScope.launch {
            repository.saveQuizProgress(
                UserQuizProgressEntity(
                    userId = userId,
                    quizId = quizId,
                    score = finalScore,
                    completed = true,
                    completedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
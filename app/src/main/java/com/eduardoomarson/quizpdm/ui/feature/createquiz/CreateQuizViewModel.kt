package com.eduardoomarson.quizpdm.ui.feature.createquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardoomarson.quizpdm.data.local.entities.QuestionEntity
import com.eduardoomarson.quizpdm.data.local.entities.QuizEntity
import com.eduardoomarson.quizpdm.data.remote.firestore.FirestoreRepository
import com.eduardoomarson.quizpdm.data.repository.QuizRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateQuizViewModel @Inject constructor(
    private val repository: QuizRepository,
    private val firestoreRepo: FirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateQuizUiState())
    val uiState: StateFlow<CreateQuizUiState> = _uiState.asStateFlow()

    fun onEvent(event: CreateQuizEvent) {
        when (event) {
            is CreateQuizEvent.OnTitleChange ->
                _uiState.update { it.copy(title = event.title) }

            is CreateQuizEvent.OnDescriptionChange ->
                _uiState.update { it.copy(description = event.description) }

            is CreateQuizEvent.OnCategoryChange ->
                _uiState.update { it.copy(selectedCategory = event.category) }

            is CreateQuizEvent.OnDifficultyChange ->
                _uiState.update { it.copy(difficulty = event.difficulty) }

            is CreateQuizEvent.OnAddQuestion -> {
                val updated = _uiState.value.questions + event.question
                _uiState.update { it.copy(questions = updated) }
            }

            is CreateQuizEvent.OnRemoveQuestion -> {
                val updated = _uiState.value.questions.toMutableList()
                    .also { it.removeAt(event.index) }
                _uiState.update { it.copy(questions = updated) }
            }

            is CreateQuizEvent.OnSaveQuiz -> saveQuiz()
        }
    }

    private fun saveQuiz() {
        val state = _uiState.value

        // Validações
        if (state.title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Digite o título do quiz") }
            return
        }
        if (state.selectedCategory.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Selecione uma categoria") }
            return
        }
        if (state.questions.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Adicione pelo menos uma questão") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Geração de um ID baseado no timestamp
                val quizId = System.currentTimeMillis().toString()
                val now = System.currentTimeMillis()

                // Cria as entidades de questão
                val questionEntities = state.questions.mapIndexed { index, q ->
                    QuestionEntity(
                        id = "${quizId}_${index}",
                        quizId = quizId,
                        question = q.question,
                        answer1 = q.answer1,
                        answer2 = q.answer2,
                        answer3 = q.answer3,
                        answer4 = q.answer4,
                        correctAnswer = q.correctAnswer,
                        score = 5,
                        picPath = q.picPath
                    )
                }

                val questionIds = questionEntities.map { it.id }

                // Cria a entidade do quiz
                val quizEntity = QuizEntity(
                    id = quizId,
                    title = state.title,
                    description = state.description,
                    category = state.selectedCategory,
                    difficulty = state.difficulty,
                    imageUrl = "",
                    questionIds = Gson().toJson(questionIds),
                    totalScore = questionEntities.size * 5,
                    createdAt = now
                )

                // Salva no Firestore e Room
                firestoreRepo.saveQuiz(quizEntity)
                firestoreRepo.saveQuestions(questionEntities)
                repository.saveQuizLocally(quizEntity, questionEntities)

                _uiState.update { it.copy(isLoading = false, isSaved = true) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Erro ao salvar: ${e.message}")
                }
            }
        }
    }
}
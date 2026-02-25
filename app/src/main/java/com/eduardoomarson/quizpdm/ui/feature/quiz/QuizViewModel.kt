package com.eduardoomarson.quizpdm.ui.feature.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardoomarson.quizpdm.data.local.entities.HistoryEntity
import com.eduardoomarson.quizpdm.ui.feature.questions.QuestionModel
import com.eduardoomarson.quizpdm.data.local.entities.UserQuizProgressEntity
import com.eduardoomarson.quizpdm.data.repository.QuizRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var startTimeMillis = 0L

    // Carrega quiz aleatório
    fun loadRandomQuiz() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            startTimeMillis = System.currentTimeMillis()
            try {
                android.util.Log.d("QuizVM", "Buscando quizzes...")
                val allQuizzes = repository.getAllQuizzesOnce()
                android.util.Log.d("QuizVM", "Quizzes encontrados: ${allQuizzes.size}")
                if (allQuizzes.isEmpty()) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Nenhum quiz disponível") }
                    return@launch
                }
                val randomQuiz = allQuizzes.random()
                android.util.Log.d("QuizVM", "Quiz selecionado: ${randomQuiz.id} - ${randomQuiz.title}")
                loadQuestionsForQuiz(randomQuiz.id)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    // Carrega quiz por categoria
    fun loadQuizByCategory(category: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            startTimeMillis = System.currentTimeMillis()
            try {
                android.util.Log.d("QuizVM", "Buscando categoria: $category")
                val quizzesByCategory = repository.getQuizzesByCategory(category)
                android.util.Log.d("QuizVM", "Quizzes na categoria: ${quizzesByCategory.size}")
                if (quizzesByCategory.isEmpty()) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Nenhum quiz nessa categoria") }
                    return@launch
                }
                val randomFromCategory = quizzesByCategory.random()
                loadQuestionsForQuiz(randomFromCategory.id)
            } catch (e: Exception) {
                android.util.Log.e("QuizVM", "Erro: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    private suspend fun loadQuestionsForQuiz(quizId: String) {
        try {
            android.util.Log.d("QuizVM", "Buscando questões do quiz: $quizId")
            val questionEntities = repository.getQuestionsForQuiz(quizId)
            android.util.Log.d("QuizVM", "Questões encontradas: ${questionEntities.size}")

            val questions = questionEntities.map { entity ->
                QuestionModel(
                    id = entity.id,
                    question = entity.question,
                    answer_1 = entity.answer1,
                    answer_2 = entity.answer2,
                    answer_3 = entity.answer3,
                    answer_4 = entity.answer4,
                    correctAnswer = entity.correctAnswer,
                    score = entity.score,
                    picPath = entity.picPath,
                    clickedAnswer = null
                )
            }

            val quiz = repository.getQuizById(quizId)

            android.util.Log.d("QuizVM", "Questions mapeadas: ${questions.size}")
            _uiState.update {
                it.copy(
                    questions = questions,
                    quizId = quizId,
                    quizCategory = quiz?.category ?: "",
                    isLoading = false,
                    currentIndex = 0,
                    score = 0
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("QuizVM", "Erro ao carregar questões: ${e.message}", e)
            _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao carregar questões: ${e.message}") }
        }
    }

    fun onAnswerSelected(answerLetter: String) {
        val state = _uiState.value
        val currentQuestion = state.questions[state.currentIndex]
        val updatedQuestions = state.questions.toMutableList()
        updatedQuestions[state.currentIndex] = currentQuestion.copy(clickedAnswer = answerLetter)
        val scoreToAdd = if (answerLetter == currentQuestion.correctAnswer) 5 else 0
        _uiState.update {
            it.copy(questions = updatedQuestions, score = it.score + scoreToAdd)
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.size - 1) {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
        }
    }

    fun previousQuestion() {
        val state = _uiState.value
        if (state.currentIndex > 0) {
            _uiState.update { it.copy(currentIndex = it.currentIndex - 1) }
        }
    }

    fun loadQuizById(quizId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            startTimeMillis = System.currentTimeMillis()
            loadQuestionsForQuiz(quizId)
        }
    }

    fun onQuizFinished(finalScore: Int, answeredQuestions: List<QuestionModel>) {
        val state = _uiState.value
        val userId = currentUser?.uid ?: return
        val timeSeconds = (System.currentTimeMillis() - startTimeMillis) / 1000
        val correctAnswers = answeredQuestions.count {
            it.clickedAnswer == it.correctAnswer
        }
        val totalQuestions = answeredQuestions.size
        val maxScore = answeredQuestions.sumOf { it.score }

        android.util.Log.d("ScoreDebug", "finalScore: $finalScore")
        android.util.Log.d("ScoreDebug", "correctAnswers: $correctAnswers")
        android.util.Log.d("ScoreDebug", "totalQuestions: $totalQuestions")
        android.util.Log.d("ScoreDebug", "timeSeconds: $timeSeconds")
        android.util.Log.d("ScoreDebug", "maxScore: $maxScore")
        android.util.Log.d("ScoreDebug", "questions com clickedAnswer: ${state.questions.map { it.clickedAnswer }}")

        _uiState.update {
            it.copy(
                isFinished = true,
                score = finalScore,
                correctAnswers = correctAnswers,
                totalQuestions = totalQuestions,
                timeSeconds = timeSeconds,
                maxScore = maxScore
            )
        }

        viewModelScope.launch {
            // Salva UserQuizProgress
            repository.saveQuizProgress(
                UserQuizProgressEntity(
                    userId = userId,
                    quizId = state.quizId,
                    score = finalScore,
                    completed = true,
                    completedAt = System.currentTimeMillis()
                )
            )

            // Salva histórico do Quiz
            val quiz = repository.getQuizById(state.quizId)
            repository.saveHistory(
                HistoryEntity(
                    id = "${userId}_${state.quizId}_${System.currentTimeMillis()}",
                    userId = userId,
                    quizId = state.quizId,
                    quizTitle = quiz?.title ?: "Quiz",
                    category = quiz?.category ?: "",
                    totalQuestions = state.questions.size,
                    correctAnswers = correctAnswers,
                    score = finalScore,
                    maxScore = state.questions.size * 5,
                    timeSeconds = timeSeconds
                )
            )

            repository.addScoreToUser(userId, finalScore)
        }
    }
}
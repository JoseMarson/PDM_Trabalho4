package com.eduardoomarson.quizpdm.data.repository

import com.eduardoomarson.quizpdm.data.local.dao.*
import com.eduardoomarson.quizpdm.data.local.entities.*
import com.eduardoomarson.quizpdm.data.remote.firestore.FirestoreRepository
import kotlinx.coroutines.flow.Flow

class QuizRepository(
    private val quizDao: QuizDao,
    private val questionDao: QuestionDao,
    private val userDao: UserDao,
    private val progressDao: UserQuizProgressDao,
    private val firestoreRepo: FirestoreRepository = FirestoreRepository()
) {

    // ── Para salvar Quizzes criados ───────────────────────────────────────────────
    suspend fun saveQuizLocally(quiz: QuizEntity, questions: List<QuestionEntity>) {
        quizDao.upsertQuiz(quiz)
        questionDao.upsertAllQuestions(questions)
    }

    // ── Quizzes ───────────────────────────────────────────────

    fun getAllQuizzes(): Flow<List<QuizEntity>> = quizDao.getAllQuizzes()

    suspend fun getQuestionsForQuiz(quizId: Int): List<QuestionEntity> =
        questionDao.getQuestionsByQuizIdOnce(quizId)

    // ── Pegar os quizzes todos e por categoria ─────────────────────
    suspend fun getAllQuizzesOnce(): List<QuizEntity> =
        quizDao.getAllQuizzesOnce()

    suspend fun getQuizzesByCategory(category: String): List<QuizEntity> =
        quizDao.getQuizzesByCategory(category)

    // ── Atualização do score de usuário local e na nuvem ao finalizar quiz ───────────────────────────────────
    suspend fun addScoreToUser(userId: String, scoreToAdd: Int) {
        val user = userDao.getUserById(userId) ?: return
        val updated = user.copy(totalScore = user.totalScore + scoreToAdd)
        userDao.upsertUser(updated)
        firestoreRepo.saveUser(updated)
    }

    // ── Sync ao fazer login ───────────────────────────────────

    suspend fun syncOnLogin(userId: String) {
        syncUserFromCloud(userId)
        syncQuizzesAndQuestionsFromCloud()
        syncUserProgressFromCloud(userId)
    }

    private suspend fun syncUserFromCloud(userId: String) {
        val remoteUser = firestoreRepo.fetchUser(userId) ?: return
        userDao.upsertUser(remoteUser)
    }

    private suspend fun syncQuizzesAndQuestionsFromCloud() {
        val remoteQuizzes = firestoreRepo.fetchAllQuizzes()
        quizDao.upsertAllQuizzes(remoteQuizzes)

        remoteQuizzes.forEach { quiz ->
            val questions = firestoreRepo.fetchQuestionsByQuizId(quiz.id)
            questionDao.upsertAllQuestions(questions)
        }
    }

    private suspend fun syncUserProgressFromCloud(userId: String) {
        val progressList = firestoreRepo.fetchUserProgress(userId)
        progressDao.upsertAllProgress(progressList)
    }

    // ── Salvar progresso (local + nuvem) ─────────────────────

    suspend fun saveQuizProgress(progress: UserQuizProgressEntity) {
        progressDao.upsertProgress(progress)          // salva local
        firestoreRepo.saveProgress(progress)          // salva na nuvem
    }

    fun getUserProgress(userId: String): Flow<List<UserQuizProgressEntity>> =
        progressDao.getProgressByUser(userId)
}
package com.eduardoomarson.quizpdm.data.remote.firestore

import com.eduardoomarson.quizpdm.data.local.entities.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // ── Criação de Questões e Quizzes ──────────────────────────────────────────────
    suspend fun saveQuiz(quiz: QuizEntity) {
        db.collection("quizzes")
            .document(quiz.id.toString())
            .set(quiz)
            .await()
    }

    suspend fun saveQuestions(questions: List<QuestionEntity>) {
        questions.forEach { question ->
            db.collection("questions")
                .document(question.id.toString())
                .set(question)
                .await()
        }
    }

    // ── Carregamento dos Quizzes ──────────────────────────────────────────────

    suspend fun fetchAllQuizzes(): List<QuizEntity> {
        return db.collection("quizzes")
            .get().await()
            .documents.mapNotNull { doc ->
                doc.toObject(QuizEntity::class.java)
            }
    }

    suspend fun fetchQuestionsByQuizId(quizId: Int): List<QuestionEntity> {
        return db.collection("questions")
            .whereEqualTo("quizId", quizId)
            .get().await()
            .documents.mapNotNull { it.toObject(QuestionEntity::class.java) }
    }

    // ── Usuário ───────────────────────────────────────────────

    suspend fun fetchUser(userId: String): UserEntity? {
        return db.collection("users")
            .document(userId)
            .get().await()
            .toObject(UserEntity::class.java)
    }

    suspend fun saveUser(user: UserEntity) {
        db.collection("users")
            .document(user.id)
            .set(user)
            .await()
    }

    // ── Progresso ─────────────────────────────────────────────

    suspend fun fetchUserProgress(userId: String): List<UserQuizProgressEntity> {
        return db.collection("user_quiz_progress")
            .whereEqualTo("userId", userId)
            .get().await()
            .documents.mapNotNull { it.toObject(UserQuizProgressEntity::class.java) }
    }

    suspend fun saveProgress(progress: UserQuizProgressEntity) {
        val docId = "${progress.userId}_${progress.quizId}"
        db.collection("user_quiz_progress")
            .document(docId)
            .set(progress)
            .await()
    }
}
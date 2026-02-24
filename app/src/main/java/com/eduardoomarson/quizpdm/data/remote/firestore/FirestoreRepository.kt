package com.eduardoomarson.quizpdm.data.remote.firestore

import com.eduardoomarson.quizpdm.data.local.entities.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // ── Criação de Questões e Quizzes ──────────────────────────────────────────────
    suspend fun saveQuiz(quiz: QuizEntity) {
        db.collection("quizzes")
            .document(quiz.id)
            .set(quiz)
            .await()
    }

    suspend fun saveQuestions(questions: List<QuestionEntity>) {
        questions.forEach { question ->
            db.collection("questions")
                .document(question.id)
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

    suspend fun fetchQuestionsByQuizId(quizId: String): List<QuestionEntity> {
        return db.collection("questions")
            .whereEqualTo("quizId", quizId)
            .get().await()
            .documents.mapNotNull { it.toObject(QuestionEntity::class.java) }
    }

    // ── Listener em tempo real para quizzes ──────────────────────────────────────────────

    fun observeQuizzes(onUpdate: (List<QuizEntity>) -> Unit): ListenerRegistration {
        return db.collection("quizzes")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val quizzes = snapshot.documents.mapNotNull {
                    it.toObject(QuizEntity::class.java)
                }
                onUpdate(quizzes)
            }
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

    // ── Histórico de Usuário ─────────────────────────────────────────────

    suspend fun saveHistory(history: HistoryEntity) {
        db.collection("quiz_history")
            .document(history.id)
            .set(history)
            .await()
    }

    suspend fun fetchHistoryByUser(userId: String): List<HistoryEntity> {
        return db.collection("quiz_history")
            .whereEqualTo("userId", userId)
            .get().await()
            .documents.mapNotNull { it.toObject(HistoryEntity::class.java) }
    }
}
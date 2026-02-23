package com.eduardoomarson.quizpdm.data.local.dao

import androidx.room.*
import com.eduardoomarson.quizpdm.data.local.entities.QuizEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Query("SELECT * FROM quizzes")
    fun getAllQuizzes(): Flow<List<QuizEntity>>

    @Query("SELECT * FROM quizzes WHERE id = :quizId")
    suspend fun getQuizById(quizId: Int): QuizEntity?

    @Query("SELECT * FROM quizzes")
    suspend fun getAllQuizzesOnce(): List<QuizEntity>

    @Query("SELECT * FROM quizzes WHERE category = :category")
    suspend fun getQuizzesByCategory(category: String): List<QuizEntity>

    @Upsert
    suspend fun upsertQuiz(quiz: QuizEntity)

    @Upsert
    suspend fun upsertAllQuizzes(quizzes: List<QuizEntity>)

    @Delete
    suspend fun deleteQuiz(quiz: QuizEntity)

    @Query("DELETE FROM quizzes")
    suspend fun deleteAllQuizzes()

}
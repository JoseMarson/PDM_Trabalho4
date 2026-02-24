package com.eduardoomarson.quizpdm.data.local.dao

import androidx.room.*
import com.eduardoomarson.quizpdm.data.local.entities.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions WHERE quizId = :quizId")
    fun getQuestionsByQuizId(quizId: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE quizId = :quizId")
    suspend fun getQuestionsByQuizIdOnce(quizId: String): List<QuestionEntity>

    @Upsert
    suspend fun upsertAllQuestions(questions: List<QuestionEntity>)

    @Query("DELETE FROM questions WHERE quizId = :quizId")
    suspend fun deleteQuestionsByQuizId(quizId: String)
}
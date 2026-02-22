package com.eduardoomarson.quizpdm.data.local.dao

import androidx.room.*
import com.eduardoomarson.quizpdm.data.local.entities.UserQuizProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserQuizProgressDao {

    @Query("SELECT * FROM user_quiz_progress WHERE userId = :userId")
    fun getProgressByUser(userId: String): Flow<List<UserQuizProgressEntity>>

    @Query("SELECT * FROM user_quiz_progress WHERE userId = :userId AND quizId = :quizId")
    suspend fun getProgress(userId: String, quizId: Int): UserQuizProgressEntity?

    @Upsert
    suspend fun upsertProgress(progress: UserQuizProgressEntity)

    @Upsert
    suspend fun upsertAllProgress(progressList: List<UserQuizProgressEntity>)
}
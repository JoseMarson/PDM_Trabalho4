package com.eduardoomarson.quizpdm.data.local.dao

import androidx.room.*
import com.eduardoomarson.quizpdm.data.local.entities.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM quiz_history WHERE userId = :userId ORDER BY playedAt DESC")
    fun getHistoryByUser(userId: String): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM quiz_history WHERE userId = :userId ORDER BY playedAt DESC")
    suspend fun getHistoryByUserOnce(userId: String): List<HistoryEntity>

    @Upsert
    suspend fun upsertHistory(history: HistoryEntity)

    @Upsert
    suspend fun upsertAllHistory(historyList: List<HistoryEntity>)

    @Query("DELETE FROM quiz_history WHERE userId = :userId")
    suspend fun deleteHistoryByUser(userId: String)
}
package com.eduardoomarson.quizpdm.data.local.dao

import androidx.room.*
import com.eduardoomarson.quizpdm.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Upsert
    suspend fun upsertUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    // LLM: CLAUDE
    // PROMPT: Finalizei o quiz mas o score não foi atualizado na HomeScreen, quais mudanças devem ser realizadas?
    // INICIO SUGESTÃO CLAUDE
    @Query("SELECT * FROM users WHERE id = :userId")
    fun observeUser(userId: String): Flow<UserEntity?>
    // Fim sugestão CLAUDE.
}
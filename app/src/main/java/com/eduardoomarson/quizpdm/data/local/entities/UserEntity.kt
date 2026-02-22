package com.eduardoomarson.quizpdm.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,   // uid do Firebase Auth
    val name: String,
    val pic: String,
    val totalScore: Int,
    val lastSyncAt: Long = 0L
)
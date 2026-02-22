package com.eduardoomarson.quizpdm.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eduardoomarson.quizpdm.data.local.dao.*
import com.eduardoomarson.quizpdm.data.local.entities.*

@Database(
    entities = [
        QuizEntity::class,
        QuestionEntity::class,
        UserEntity::class,
        UserQuizProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun quizDao(): QuizDao
    abstract fun questionDao(): QuestionDao
    abstract fun userDao(): UserDao
    abstract fun userQuizProgressDao(): UserQuizProgressDao

    companion object {
        @Volatile private var INSTANCE: QuizDatabase? = null

        fun getInstance(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
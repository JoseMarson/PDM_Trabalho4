package com.eduardoomarson.quizpdm.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.eduardoomarson.quizpdm.data.local.dao.*
import com.eduardoomarson.quizpdm.data.local.entities.*

@Database(
    entities = [
        QuizEntity::class,
        QuestionEntity::class,
        UserEntity::class,
        UserQuizProgressEntity::class,
        HistoryEntity::class
    ],
    version = 2,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun quizDao(): QuizDao
    abstract fun questionDao(): QuestionDao
    abstract fun userDao(): UserDao
    abstract fun userQuizProgressDao(): UserQuizProgressDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile private var INSTANCE: QuizDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                CREATE TABLE IF NOT EXISTS quiz_history (
                    id TEXT NOT NULL PRIMARY KEY,
                    userId TEXT NOT NULL,
                    quizId INTEGER NOT NULL,
                    quizTitle TEXT NOT NULL,
                    category TEXT NOT NULL,
                    totalQuestions INTEGER NOT NULL,
                    correctAnswers INTEGER NOT NULL,
                    score INTEGER NOT NULL,
                    maxScore INTEGER NOT NULL,
                    timeSeconds INTEGER NOT NULL,
                    playedAt INTEGER NOT NULL
                )
            """)
            }
        }

        fun getInstance(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
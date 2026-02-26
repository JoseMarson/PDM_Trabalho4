package com.eduardoomarson.quizpdm.di

import android.content.Context
import com.eduardoomarson.quizpdm.data.local.dao.HistoryDao
import com.eduardoomarson.quizpdm.data.local.dao.QuestionDao
import com.eduardoomarson.quizpdm.data.local.dao.QuizDao
import com.eduardoomarson.quizpdm.data.local.dao.UserDao
import com.eduardoomarson.quizpdm.data.local.dao.UserQuizProgressDao
import com.eduardoomarson.quizpdm.data.local.database.QuizDatabase
import com.eduardoomarson.quizpdm.data.remote.firestore.FirestoreRepository
import com.eduardoomarson.quizpdm.data.repository.QuizRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuizDatabase(@ApplicationContext context: Context): QuizDatabase =
        QuizDatabase.getInstance(context)

    @Provides fun provideQuizDao(db: QuizDatabase): QuizDao = db.quizDao()
    @Provides fun provideQuestionDao(db: QuizDatabase): QuestionDao = db.questionDao()
    @Provides fun provideUserDao(db: QuizDatabase): UserDao = db.userDao()
    @Provides fun provideProgressDao(db: QuizDatabase): UserQuizProgressDao = db.userQuizProgressDao()

    @Provides
    @Singleton
    fun provideHistoryDao(db: QuizDatabase): HistoryDao = db.historyDao()

    @Provides
    @Singleton
    fun provideFirestoreRepository(): FirestoreRepository = FirestoreRepository()

    @Provides
    @Singleton
    fun provideQuizRepository(
        quizDao: QuizDao,
        questionDao: QuestionDao,
        userDao: UserDao,
        progressDao: UserQuizProgressDao,
        historyDao: HistoryDao,
        firestoreRepo: FirestoreRepository
    ): QuizRepository = QuizRepository(quizDao, questionDao, userDao, progressDao, historyDao, firestoreRepo)
}

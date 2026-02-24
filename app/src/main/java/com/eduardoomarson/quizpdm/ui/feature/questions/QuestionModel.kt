package com.eduardoomarson.quizpdm.ui.feature.questions

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionModel(
    val id: String = "",
    val question: String?,
    val answer_1: String?,
    val answer_2: String?,
    val answer_3: String?,
    val answer_4: String?,
    val correctAnswer: String?,
    val score: Int,
    val picPath: String?,
    val clickedAnswer: String?,
) : Parcelable
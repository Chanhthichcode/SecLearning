package com.android.seclearning.data.response

import com.google.gson.annotations.SerializedName

data class QuizSubmitResponse(
    val message: String,
    val summary: QuizResultSummary,
    val details: List<QuizResultDetail>
)

data class QuizResultSummary(
    val total: Int,
    val correct: Int,
    val wrong: Int,
    val percentage: String,
    val points: Float
)

data class QuizResultDetail(
    val questionId: Int,

    @SerializedName("is_correct")
    val isCorrect: Boolean,

    @SerializedName("user_answer_index")
    val userAnswerIndex: Int,

    @SerializedName("correct_answer_index")
    val correctAnswerIndex: Int
)


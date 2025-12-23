package com.android.seclearning.data.request

import com.google.gson.annotations.SerializedName

data class QuizAnswerRequest(
    val id: Int,

    @SerializedName("user_answer")
    val userAnswer: Int
)

data class QuizSubmitRequest(
    val userId: String,
    val domain: String,
    val submissions: List<QuizAnswerRequest>
)

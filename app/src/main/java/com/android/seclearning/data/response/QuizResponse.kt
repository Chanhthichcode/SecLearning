package com.android.seclearning.data.response

import com.android.seclearning.data.model.QuizModel

data class QuizResponse(
    val count: Int,
    val data: List<QuizModel>
)

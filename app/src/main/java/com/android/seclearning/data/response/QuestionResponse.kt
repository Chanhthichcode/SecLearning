package com.android.seclearning.data.response

import com.android.seclearning.data.model.QuestionModel
import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("data")
    val `data`: List<QuestionModel>?,
)

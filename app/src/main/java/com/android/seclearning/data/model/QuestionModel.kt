package com.android.seclearning.data.model

import com.google.gson.annotations.SerializedName

data class QuestionModel(
    @SerializedName("_id")
    val id: String,

    val questionId: Int,
    val domain: String,
    val question: String,

    val options: List<String>,
    val mapping: List<String>,

    @SerializedName("__v")
    val version: Int,

    val createdAt: String,
    val updatedAt: String,
    var selectedOption: Int = -1,
)

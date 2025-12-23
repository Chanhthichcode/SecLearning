package com.android.seclearning.data.model

data class QuizModel(
    val id: Int,
    val domain: String,
    val skill: String,
    val difficulty: String,
    val type: String,
    val question: String,
    val options: List<String>,
    val answer: Int,
    var selectedOption: Int = -1
)

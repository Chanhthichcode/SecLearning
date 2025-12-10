package com.android.seclearning.data.model

data class QuestionModel(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val domain: String,
    val mapping: List<String>,
    val options: List<String>,
    val question: String,
    val questionId: Int,
    val updatedAt: String
)
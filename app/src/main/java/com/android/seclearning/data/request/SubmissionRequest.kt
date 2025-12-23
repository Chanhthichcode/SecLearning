package com.android.seclearning.data.request

data class AnswerRequest(
    val questionId: Int,
    val optionIndex: Int
)

data class SubmissionRequest(
    val studentId: String,
    val studentName: String,
    val answers: List<AnswerRequest>
)

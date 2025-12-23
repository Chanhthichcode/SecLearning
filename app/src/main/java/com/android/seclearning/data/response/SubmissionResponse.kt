package com.android.seclearning.data.response

data class SubmissionResponse(
    val submissionId: String,
    val result: List<String>,
    val resultStats: ResultStats
)

data class ResultStats(
    val counts: Map<String, Int>,
    val percentages: Map<String, Double>
)


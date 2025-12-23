package com.android.seclearning.data.request

data class RegisterRoadmapRequest (
    val studentId: String,
    val career: String,
    val completedItems: Int?
)
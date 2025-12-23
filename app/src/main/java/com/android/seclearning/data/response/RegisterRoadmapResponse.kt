package com.android.seclearning.data.response

import com.google.gson.annotations.SerializedName

data class RegisterRoadmapResponse(
    val message: String,
    val data: ProgressData
)

data class ProgressData(
    @SerializedName("studentId")
    val studentId: String,

    @SerializedName("career")
    val career: String,

    @SerializedName("completedItems")
    val completedItems: List<Int>,

    @SerializedName("_id")
    val id: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("__v")
    val version: Int
)

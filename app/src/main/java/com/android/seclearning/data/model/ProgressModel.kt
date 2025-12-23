package com.android.seclearning.data.model

import com.google.gson.annotations.SerializedName

data class ProgressModel(
    @SerializedName("_id")
    val id: String,

    @SerializedName("studentId")
    val studentId: String,

    @SerializedName("career")
    val career: String,

    @SerializedName("completedItems")
    val completedItems: List<Int>,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("__v")
    val version: Int
)

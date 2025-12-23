package com.android.seclearning.data.request

import com.google.gson.annotations.SerializedName

data class ProgressRequest(
    val studentId: String,

    val career: String,

    val itemIndex: Int,
)
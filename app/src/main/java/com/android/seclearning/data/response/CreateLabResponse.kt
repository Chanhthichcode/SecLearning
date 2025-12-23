package com.android.seclearning.data.response

import com.android.seclearning.data.model.LabModel
import com.google.gson.annotations.SerializedName

data class CreateLabResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: LabModel
)

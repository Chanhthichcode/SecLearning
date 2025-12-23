package com.android.seclearning.data.response

import com.android.seclearning.data.model.LabModel
import com.google.gson.annotations.SerializedName

data class LabResponse(
    @SerializedName("count")
    val count: Int,

    @SerializedName("data")
    val data: List<LabModel>
)

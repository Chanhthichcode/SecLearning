package com.android.seclearning.data.response

import com.android.seclearning.data.model.ProgressModel

data class PutProgressResponse(
    val success: Boolean,
    val progress: ProgressModel
)

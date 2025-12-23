package com.android.seclearning.data.request

import com.google.gson.annotations.SerializedName

data class CreatedResourceRequest(
    val category: String,
    val title: String,
    val language: String,
    val level: String,
    val url: String,
    val notes: String,
    val platform: String
)

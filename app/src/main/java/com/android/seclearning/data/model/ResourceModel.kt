package com.android.seclearning.data.model

import com.google.gson.annotations.SerializedName

data class ResourceModel(
    @SerializedName("_id")
    val id: String,

    val category: String,
    val title: String,
    val language: String,
    val level: String,
    val url: String,
    val notes: String,

    val platform: String,

    val createdAt: String,
    val updatedAt: String,

    @SerializedName("__v")
    val version: Int
)

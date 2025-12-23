package com.android.seclearning.data.model

import com.google.gson.annotations.SerializedName

data class LabModel(
    @SerializedName("_id")
    val mongoId: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("platform")
    val platform: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("skill_tags")
    val skillTags: List<String>,

    @SerializedName("difficulty")
    val difficulty: Int,

    @SerializedName("subject")
    val subject: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("description_detail")
    val descriptionDetail: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("__v")
    val version: Int
)

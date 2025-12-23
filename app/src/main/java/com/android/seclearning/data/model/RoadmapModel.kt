package com.android.seclearning.data.model

data class RoadmapModel(
    val _id: String,
    val career: String,
    val name: String,
    val description: String,
    val version: String,
    val levels: List<TaskLevelModel>,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

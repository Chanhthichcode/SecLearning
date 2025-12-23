package com.android.seclearning.data.model

data class TaskLevelModel(
    val level: Int,
    val slug: String,
    val name: String,
    val goals: List<String>,
    val labs: List<LabRoadmapModel>
)


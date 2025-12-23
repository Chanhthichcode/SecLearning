package com.android.seclearning.data.response

import com.android.seclearning.data.model.RoadmapModel

data class RoadmapResponse(
    val completed: Int,
    val roadmap: RoadmapModel,
    val completedItems: List<Int>
)

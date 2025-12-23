package com.android.seclearning.data.response

import com.android.seclearning.data.model.ResourceModel

data class ResourceResponse(
    val count: Int,
    val data: List<ResourceModel>
)

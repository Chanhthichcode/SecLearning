package com.android.seclearning.data.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("hoTen")
    val hoTen: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("message")
    val message: String? = null
)

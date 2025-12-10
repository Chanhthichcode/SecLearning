package com.android.seclearning.data.response

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("matKhau")
    val password: String,
)
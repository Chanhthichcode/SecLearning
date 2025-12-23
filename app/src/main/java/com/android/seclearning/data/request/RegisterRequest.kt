package com.android.seclearning.data.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("hoTen")
    val name: String,

    @SerializedName("matKhau")
    val password: String,

    @SerializedName("nhapLaiMatKhau")
    val confirmPassword: String
)
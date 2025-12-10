package com.android.seclearning.data.response

import com.android.seclearning.data.model.QuestionModel
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
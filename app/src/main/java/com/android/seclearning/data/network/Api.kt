package com.android.seclearning.data.network

import com.android.seclearning.data.model.QuestionModel
import com.android.seclearning.data.model.UserModel
import com.android.seclearning.data.response.LoginRequest
import com.android.seclearning.data.response.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface Api {
    @POST
    suspend fun registerUser(
        @Url url: String,
        @Body request: RegisterRequest
    ): UserModel

    @POST
    suspend fun loginUser(
        @Url url: String,
        @Body  request: LoginRequest
    ): UserModel

    @GET
    suspend fun getListQuestion(
        @Url url: String,
    ): List<QuestionModel>
}
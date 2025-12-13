package com.android.seclearning.data.repository

import com.android.seclearning.common.utils.runInIO
import com.android.seclearning.data.model.QuestionModel
import com.android.seclearning.data.model.UserModel
import com.android.seclearning.data.network.Api
import com.android.seclearning.data.network.END_POINT_GET_QUESTION
import com.android.seclearning.data.network.END_POINT_LOGIN
import com.android.seclearning.data.network.END_POINT_REGISTER
import com.android.seclearning.data.network.generateServerAPI
import com.android.seclearning.data.response.LoginRequest
import com.android.seclearning.data.response.RegisterRequest
import javax.inject.Inject

class HomeDataRepository @Inject constructor(
    private val api: Api
) {
    suspend fun registerUser(
        email: String,
        username: String,
        password: String,
        rePassword: String
    ): UserModel {
        return runInIO { fetchRegisterRemote(email, username, password, rePassword) }
    }

    private suspend fun fetchRegisterRemote(
        email: String,
        username: String,
        password: String,
        rePassword: String
    ): UserModel {

        val url: String = generateServerAPI(END_POINT_REGISTER)

        return api.registerUser(
            url = url,
            request = RegisterRequest(
                email = email,
                name = username,
                password = password,
                confirmPassword = rePassword
            )
        )
    }

    suspend fun loginUser(
        email: String,
        password: String
    ): UserModel {
        return runInIO { fetchLoginRemote(email, password) }
    }

    private suspend fun fetchLoginRemote(
        email: String,
        password: String
    ): UserModel {

        val url: String = generateServerAPI(END_POINT_LOGIN)

        return api.loginUser(
            url = url,
            request = LoginRequest(
                email = email,
                password = password
            )
        )
    }

    suspend fun getListQuestions(): List<QuestionModel> {
        val dataFromApi = runInIO { fetchListQuestions() }
        return dataFromApi
    }

    private suspend fun fetchListQuestions(): List<QuestionModel> {
        val url: String = generateServerAPI(END_POINT_GET_QUESTION)
        val res = api.getListQuestion(
            url = url,
        )
        return res
    }
}
package com.android.seclearning.data.network

import com.android.seclearning.data.model.QuestionModel
import com.android.seclearning.data.model.RoadmapModel
import com.android.seclearning.data.model.UserModel
import com.android.seclearning.data.request.CreateLabRequest
import com.android.seclearning.data.request.CreatedResourceRequest
import com.android.seclearning.data.request.LoginRequest
import com.android.seclearning.data.request.ProgressRequest
import com.android.seclearning.data.request.QuizSubmitRequest
import com.android.seclearning.data.request.RegisterRequest
import com.android.seclearning.data.request.RegisterRoadmapRequest
import com.android.seclearning.data.request.SubmissionRequest
import com.android.seclearning.data.response.CreateLabResponse
import com.android.seclearning.data.response.CreateResourceResponse
import com.android.seclearning.data.response.LabResponse
import com.android.seclearning.data.response.ProgressResponse
import com.android.seclearning.data.response.PutProgressResponse
import com.android.seclearning.data.response.QuizResponse
import com.android.seclearning.data.response.QuizSubmitResponse
import com.android.seclearning.data.response.RegisterRoadmapResponse
import com.android.seclearning.data.response.ResourceResponse
import com.android.seclearning.data.response.RoadmapResponse
import com.android.seclearning.data.response.SubmissionResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
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

    @POST
    suspend fun submitQuestion(
        @Url url: String,
        @Body request: SubmissionRequest
    ): SubmissionResponse

    @GET
    suspend fun getListQuiz(
        @Url url: String,
        @Query("count") count: Int?,
        @Query("domain") domain: String?
    ): QuizResponse

    @POST
    suspend fun submitQuiz(
        @Url url: String,
        @Body request: QuizSubmitRequest
    ): QuizSubmitResponse

    @GET
    suspend fun getRoadmap(
        @Url url: String,
    ): RoadmapModel

    @POST
    suspend fun registerRoadmap(
        @Url url: String,
        @Body request: RegisterRoadmapRequest
    ): RegisterRoadmapResponse

    @GET
    suspend fun getLab(
        @Url url: String,
        @Query("platform") platform: String?,
        @Query("difficulty") difficulty: Int?,
        @Query("search") search: String?
    ): LabResponse

    @GET
    suspend fun getResource(
        @Url url: String,
        @Query("platform") platform: String?,
        @Query("category") category: String?,
        @Query("language") language: String?,
        @Query("level") level: String?,
        @Query("search") search: String?
    ): ResourceResponse

    @GET
    suspend fun getListProgress(
        @Url url: String,
    ): ProgressResponse

    @GET
    suspend fun getProgress(
        @Url url: String,
    ): RoadmapResponse

    @PUT
    suspend fun putProgress(
        @Url url: String,
        @Body request: ProgressRequest
    ): PutProgressResponse

    @POST
    suspend fun postLab(
        @Url url: String,
        @Body request: CreateLabRequest
    ): CreateLabResponse

    @POST
    suspend fun postResource(
        @Url url: String,
        @Body request: CreatedResourceRequest
    ): CreateResourceResponse
}
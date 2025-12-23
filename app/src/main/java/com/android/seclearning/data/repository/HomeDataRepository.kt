package com.android.seclearning.data.repository

import com.android.seclearning.common.utils.runInIO
import com.android.seclearning.data.model.QuestionModel
import com.android.seclearning.data.model.RoadmapModel
import com.android.seclearning.data.model.UserModel
import com.android.seclearning.data.network.Api
import com.android.seclearning.data.network.END_POINT_CREATE_LAB
import com.android.seclearning.data.network.END_POINT_GET_LAB
import com.android.seclearning.data.network.END_POINT_GET_PROGRESS
import com.android.seclearning.data.network.END_POINT_GET_QUESTION
import com.android.seclearning.data.network.END_POINT_GET_QUIZ
import com.android.seclearning.data.network.END_POINT_GET_RESOURCE
import com.android.seclearning.data.network.END_POINT_GET_ROADMAP
import com.android.seclearning.data.network.END_POINT_LOGIN
import com.android.seclearning.data.network.END_POINT_PUT_PROGRESS
import com.android.seclearning.data.network.END_POINT_REGISTER
import com.android.seclearning.data.network.END_POINT_REGISTER_ROADMAP
import com.android.seclearning.data.network.END_POINT_SUBMIT_ANSWERS
import com.android.seclearning.data.network.END_POINT_SUBMIT_QUIZ
import com.android.seclearning.data.network.generateServerAPI
import com.android.seclearning.data.request.AnswerRequest
import com.android.seclearning.data.request.CreateLabRequest
import com.android.seclearning.data.request.CreatedResourceRequest
import com.android.seclearning.data.request.LoginRequest
import com.android.seclearning.data.request.ProgressRequest
import com.android.seclearning.data.request.QuizAnswerRequest
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

    suspend fun submitAnswers(
        studentId: String,
        studentName: String,
        answers: List<AnswerRequest>
    ): SubmissionResponse {
        return runInIO {
            fetchSubmitAnswers(studentId, studentName, answers)
        }
    }

    private suspend fun fetchSubmitAnswers(
        studentId: String,
        studentName: String,
        answers: List<AnswerRequest>
    ): SubmissionResponse {

        val url = generateServerAPI(END_POINT_SUBMIT_ANSWERS)

        return api.submitQuestion(
            url = url,
            request = SubmissionRequest(
                studentId = studentId,
                studentName = studentName,
                answers = answers
            )
        )
    }

    suspend fun getListQuiz(count: Int?, domain: String?): QuizResponse {
        val dataFromApi = runInIO { fetchListQuiz(count, domain) }
        return dataFromApi
    }

    private suspend fun fetchListQuiz(count: Int?, domain: String?): QuizResponse {
        val url: String = generateServerAPI(END_POINT_GET_QUIZ)
        val res = api.getListQuiz(
            url = url,
            count = count,
            domain = domain
        )
        return res
    }

    suspend fun submitQuiz(
        userId: String,
        domain: String,
        submissions: List<QuizAnswerRequest>
    ): QuizSubmitResponse {
        return runInIO {
            fetchSubmitQuiz(userId, domain, submissions)
        }
    }

    private suspend fun fetchSubmitQuiz(
        userId: String,
        domain: String,
        submissions: List<QuizAnswerRequest>
    ): QuizSubmitResponse {

        val url = generateServerAPI(END_POINT_SUBMIT_QUIZ)

        return api.submitQuiz(
            url = url,
            request = QuizSubmitRequest(
                userId = userId,
                domain = domain,
                submissions = submissions
            )
        )
    }

    suspend fun getRoadmap(career: String): RoadmapModel {
        val dataFromApi = runInIO { fetchRoadmap(career) }
        return dataFromApi
    }

    suspend fun fetchRoadmap(career: String): RoadmapModel {
        val url = generateServerAPI(
            END_POINT_GET_ROADMAP,
            career
        )
        val res = api.getRoadmap(url = url)
        return res
    }

    suspend fun registerRoadmap(
        studentId: String,
        career: String,
        completedItems: Int?
    ): RegisterRoadmapResponse {
        return runInIO { fetchRegisterRoadmap(studentId, career, completedItems) }
    }

    private suspend fun fetchRegisterRoadmap(
        studentId: String,
        career: String,
        completedItems: Int?
    ): RegisterRoadmapResponse {

        val url: String = generateServerAPI(END_POINT_REGISTER_ROADMAP)

        return api.registerRoadmap(
            url = url,
            request = RegisterRoadmapRequest(
                studentId = studentId,
                career = career,
                completedItems = completedItems,
            )
        )
    }

    suspend fun getListLab(platform: String?, difficulty: Int?, search: String?): LabResponse {
        val dataFromApi = runInIO { fetchListLab(platform, difficulty, search) }
        return dataFromApi
    }

    private suspend fun fetchListLab(
        platform: String?,
        difficulty: Int?,
        search: String?
    ): LabResponse {
        val url: String = generateServerAPI(END_POINT_GET_LAB)
        val res = api.getLab(
            url = url,
            platform = platform,
            difficulty = difficulty,
            search = search
        )
        return res
    }

    suspend fun getListResource(
        platform: String?,
        category: String?,
        language: String?,
        level: String?,
        search: String?
    ): ResourceResponse {
        val dataFromApi = runInIO { fetchListResource(platform, category, language, level, search) }
        return dataFromApi
    }

    private suspend fun fetchListResource(
        platform: String?,
        category: String?,
        language: String?,
        level: String?,
        search: String?
    ): ResourceResponse {
        val url: String = generateServerAPI(END_POINT_GET_RESOURCE)
        val res = api.getResource(
            url = url,
            platform = platform,
            category = category,
            language = language,
            level = level,
            search = search
        )
        return res
    }

    suspend fun getListProgress(id: String): ProgressResponse {
        val dataFromApi = runInIO { fetchListProgress(id) }
        return dataFromApi
    }

    suspend fun fetchListProgress(id: String): ProgressResponse {
        val url = generateServerAPI(
            END_POINT_GET_PROGRESS,
            id
        )
        val res = api.getListProgress(url = url)
        return res
    }

    suspend fun getProgress(id: String, career: String): RoadmapResponse {
        val dataFromApi = runInIO { fetchProgress(id, career) }
        return dataFromApi
    }

    suspend fun fetchProgress(id: String, career: String): RoadmapResponse {
        val url = generateServerAPI(
            endPoint = END_POINT_GET_PROGRESS,
            id = id,
            career = career
        )
        val res = api.getProgress(url = url)
        return res
    }

    suspend fun putProgress(
        studentId: String,
        career: String,
        itemIndex: Int
    ): PutProgressResponse {
        return runInIO { fetchPutProgress(studentId, career, itemIndex) }
    }

    private suspend fun fetchPutProgress(
        studentId: String,
        career: String,
        itemIndex: Int
    ): PutProgressResponse {

        val url: String = generateServerAPI(END_POINT_PUT_PROGRESS)

        return api.putProgress(
            url = url,
            request = ProgressRequest(
                studentId = studentId,
                career = career,
                itemIndex = itemIndex
            )
        )
    }

    suspend fun postLab(
        link: String
    ): CreateLabResponse {
        return runInIO {
            fetchPostLab(link)
        }
    }

    private suspend fun fetchPostLab(
        link: String,
    ): CreateLabResponse {

        val url: String = generateServerAPI(END_POINT_CREATE_LAB)

        return api.postLab(
            url = url,
            request = CreateLabRequest(link)
        )
    }

    suspend fun postResource(
        title: String,
        category: String,
        language: String,
        link: String,
        level: String,
        notes: String,
        platform: String
    ): CreateResourceResponse {
        return runInIO {
            fetchPostResource(
                title = title,
                category = category,
                language = language,
                level = level,
                notes = notes,
                link = link,
                platform = platform

            )
        }
    }

    private suspend fun fetchPostResource(
        title: String,
        category: String,
        language: String,
        link: String,
        level: String,
        notes: String,
        platform: String
    ): CreateResourceResponse {

        val url: String = generateServerAPI(END_POINT_CREATE_LAB)

        return api.postResource(
            url = url,
            request = CreatedResourceRequest(
                title = title,
                category = category,
                language = language,
                level = level,
                notes = notes,
                url = link,
                platform = platform
            )
        )
    }
}
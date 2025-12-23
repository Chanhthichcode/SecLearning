package com.android.seclearning.data.network

const val BASE_URL = "https://da-be-h950.onrender.com/api/auth"
const val END_POINT_REGISTER = "/register"
const val END_POINT_LOGIN = "/login"
const val END_POINT_GET_QUESTION = "/questions"
const val END_POINT_SUBMIT_ANSWERS = "/submissions/submit"
const val END_POINT_GET_QUIZ = "/get-quiz"
const val END_POINT_SUBMIT_QUIZ = "/submit-quiz"
const val END_POINT_GET_ROADMAP = "/roadmap"
const val END_POINT_REGISTER_ROADMAP = "/progress/create"
const val END_POINT_GET_LAB = "/get-labs"
const val END_POINT_GET_RESOURCE = "/get-resorce"
const val END_POINT_GET_PROGRESS = "/progress"
const val END_POINT_PUT_PROGRESS = "/progress/update"
const val END_POINT_CREATE_LAB = "/post-url"
const val END_POINT_CREATE_RESOURCE = "/post-resorce"



fun generateServerAPI(endPoint: String) = "$BASE_URL$endPoint"

fun generateServerAPI(endPoint: String, career: String): String {
    return "$BASE_URL$endPoint/$career"
}
fun generateServerAPI(endPoint: String, id:String, career: String): String {
    return "$BASE_URL$endPoint/$id/$career"
}

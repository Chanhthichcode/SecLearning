package com.android.seclearning.data.network

const val BASE_URL = "https://da-be-h950.onrender.com/api/auth"
const val END_POINT_REGISTER = "/register"
const val END_POINT_LOGIN = "/login"
const val END_POINT_GET_QUESTION = "/questions"

fun generateServerAPI(endPoint: String) = "$BASE_URL$endPoint"
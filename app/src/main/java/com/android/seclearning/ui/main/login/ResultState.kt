package com.android.seclearning.ui.main.login

import androidx.annotation.Keep

@Keep
sealed class ResultState<out T> {
    data class Success<out T>(val data: T): ResultState<T>()
    data class Error(val exception: Throwable, val code: Int = 400): ResultState<Nothing>()
    data class Loading(val message: String? = null): ResultState<Nothing>()
}

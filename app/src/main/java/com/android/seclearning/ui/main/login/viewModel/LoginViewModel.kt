package com.android.seclearning.ui.main.login.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.seclearning.data.model.UserModel
import com.android.seclearning.data.repository.AppRepository
import com.android.seclearning.data.repository.HomeDataRepository
import com.android.seclearning.ui.main.login.ResultState
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Lazy<HomeDataRepository>,
    private val appRepository: Lazy<AppRepository>
) : ViewModel() {

    private val _registerState = MutableLiveData<ResultState<UserModel>>()
    val registerState: LiveData<ResultState<UserModel>> = _registerState

    private val _loginState = MutableLiveData<ResultState<UserModel>>()
    val loginState: LiveData<ResultState<UserModel>> = _loginState

    fun register(
        email: String,
        username: String,
        password: String,
        rePassword: String
    ) {
        executeRequest(
            stateLiveData = _registerState,
            successCode = 200, // tuỳ ý, nếu muốn
            errorCode = 400
        ) {
            repository.get().registerUser(email, username, password, rePassword)
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        executeRequest(
            stateLiveData = _loginState,
            successCode = 200,
            errorCode = 401
        ) {
            repository.get().loginUser(email, password)
        }
    }

    /**
     * Hàm generic để thực hiện request và xử lý lỗi
     */
    private fun <T> executeRequest(
        stateLiveData: MutableLiveData<ResultState<T>>,
        successCode: Int,
        errorCode: Int,
        block: suspend () -> T
    ) {
        viewModelScope.launch {
            stateLiveData.value = ResultState.Loading()
            try {
                val result = block()
                if (hasErrorMessage(result)) {
                    stateLiveData.value = ResultState.Error(
                        Throwable(getMessage(result) ?: "Lỗi không xác định"),
                        errorCode
                    )
                } else {
                    if (result is UserModel) {
                        appRepository.get().saveUser(result)
                    }
                    stateLiveData.value = ResultState.Success(result)
                }
            } catch (e: HttpException) {
                stateLiveData.value = ResultState.Error(
                    Throwable(parseHttpError(e) ?: "Hệ thống đang gặp lỗi, vui lòng thử lại"),
                    e.code()
                )
            } catch (e: Exception) {
                stateLiveData.value = ResultState.Error(
                    Throwable("Hệ thống đang gặp lỗi, vui lòng thử lại"),
                    500
                )
            }
        }
    }

    /**
     * Kiểm tra object trả về có message lỗi hay không
     */
    private fun <T> hasErrorMessage(result: T): Boolean {
        return try {
            val message = getMessage(result)
            !message.isNullOrEmpty()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Lấy message từ object result nếu có
     */
    private fun <T> getMessage(result: T): String? {
        return try {
            val json = JSONObject(result.toString())
            json.optString("message", null)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Lấy message từ HttpException nếu có
     */
    private fun parseHttpError(e: HttpException): String? {
        return try {
            val errorBody = e.response()?.errorBody()?.string() // CHỈ ĐỌC 1 LẦN
            if (!errorBody.isNullOrEmpty()) {
                JSONObject(errorBody).optString("message")
            } else null
        } catch (ex: Exception) {
            null
        }
    }
}


package com.android.seclearning.ui.main.onboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.seclearning.appRepository
import com.android.seclearning.common.utils.runInBackground
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    val appStatusLoaded = MutableLiveData<Boolean>()

    fun isFirstOpenApp() = appRepository().isFirstOpenApp()
    fun isLoggedIn() = appRepository().isLoggedIn()
    fun isBuildRoadmap() = appRepository().isBuildRoadmap()

    fun getAppConfig() {
        viewModelScope.launch {
            try {
                runInBackground {
                    appRepository().checkAppStatus()
                }
                appStatusLoaded.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
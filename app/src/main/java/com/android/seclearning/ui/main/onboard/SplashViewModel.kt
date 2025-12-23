package com.android.seclearning.ui.main.onboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Lazy
import com.android.seclearning.appRepository
import com.android.seclearning.data.local.PreferencesKey
import com.android.seclearning.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val preferencesManager: Lazy<PreferencesManager>
) : ViewModel() {

    val appStatusLoaded = MutableLiveData<Boolean>()

    fun isAdmin(): Boolean =
        preferencesManager.get()
            .getBoolean(PreferencesKey.IS_ADMIN, false)

    fun isFirstOpenApp(): Boolean =
        preferencesManager.get()
            .getBoolean(PreferencesKey.IS_FIRST_TIME_OPEN_APP, true)

    fun isLoggedIn(): Boolean =
        preferencesManager.get()
            .getBoolean(PreferencesKey.IS_LOGGED_IN, false)

    fun isBuildRoadmap(): Boolean =
        preferencesManager.get()
            .getBoolean(PreferencesKey.IS_BUILD_ROADMAP, false)


    fun getAppConfig() {
        viewModelScope.launch {
            try {
                appRepository().checkAppStatus()
                appStatusLoaded.postValue(true)
            } catch (e: Exception) {
                appStatusLoaded.postValue(false)
                e.printStackTrace()
            }
        }
    }
}
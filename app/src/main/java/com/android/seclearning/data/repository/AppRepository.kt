package com.android.seclearning.data.repository

import com.android.seclearning.Constants
import com.android.seclearning.Logger
import com.android.seclearning.common.utils.backgroundLaunch
import com.android.seclearning.data.database.AppDatabase
import com.android.seclearning.data.local.PreferencesKey
import com.android.seclearning.data.local.PreferencesManager
import com.android.seclearning.data.model.UserModel
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class AppRepository @Inject constructor(
    val preferencesManager: Lazy<PreferencesManager>
) {
    private var mIsFirstOpenApp = true
    private var mIsLoggedIn = false
    private var mIsBuildRoadmap = false

    fun isFirstOpenApp() = mIsFirstOpenApp
    fun isLoggedIn() = mIsLoggedIn
    fun isBuildRoadmap() = mIsBuildRoadmap

    fun setFirstOpenAppToFalse() {
        backgroundLaunch {
            preferencesManager.get().save(PreferencesKey.IS_FIRST_TIME_OPEN_APP, false)
        }
    }

    fun setLoggedIn() {
        mIsLoggedIn = true
        backgroundLaunch {
            preferencesManager.get().save(PreferencesKey.IS_LOGGED_IN, true)
        }
    }

    fun setBuildRoadmap() {
        mIsBuildRoadmap = true
        backgroundLaunch {
            preferencesManager.get().save(PreferencesKey.IS_BUILD_ROADMAP, true)
        }
    }

    suspend fun checkAppStatus() = withContext(Dispatchers.IO) {
        mIsFirstOpenApp = preferencesManager.get()
            .getBoolean(PreferencesKey.IS_FIRST_TIME_OPEN_APP, true)
        mIsLoggedIn = preferencesManager.get()
            .getBoolean(PreferencesKey.IS_LOGGED_IN, false)
        mIsBuildRoadmap = preferencesManager.get()
            .getBoolean(PreferencesKey.IS_BUILD_ROADMAP, false)
    }


    fun getLanguageSet(): String = preferencesManager.get().getString(PreferencesKey.KEY_LANGUAGE)
        ?: Locale.getDefault().language

    fun getCountryCode() =
        preferencesManager.get().getString(PreferencesKey.COUNTRY_CODE) ?: ""

    fun saveUser(user: UserModel) {
        backgroundLaunch {
            val isAdmin = user.id == Constants.ADMIN_ID

            preferencesManager.get().apply {
                save(PreferencesKey.USER_ID, user.id.orEmpty())
                save(PreferencesKey.USER_NAME, user.hoTen.orEmpty())
                save(PreferencesKey.USER_EMAIL, user.email.orEmpty())
                save(PreferencesKey.USER_TOKEN, user.token.orEmpty())
                save(PreferencesKey.IS_LOGGED_IN, true)
                save(PreferencesKey.IS_ADMIN, isAdmin)
            }

            Logger.d("Auth", "User role = ${if (isAdmin) "ADMIN" else "MEMBER"}")
        }
    }


    fun getUserId(): String =
        preferencesManager.get().getString(PreferencesKey.USER_ID).orEmpty()

    fun getUserName(): String =
        preferencesManager.get().getString(PreferencesKey.USER_NAME).orEmpty()

    fun getUserToken(): String =
        preferencesManager.get().getString(PreferencesKey.USER_TOKEN).orEmpty()

    fun isAdmin(): Boolean = preferencesManager.get().getBoolean(PreferencesKey.IS_ADMIN, false)

    fun clearUser() {
        backgroundLaunch {
            preferencesManager.get().apply {
                save(PreferencesKey.USER_ID, "")
                save(PreferencesKey.USER_NAME, "")
                save(PreferencesKey.USER_EMAIL, "")
                save(PreferencesKey.USER_TOKEN, "")
                save(PreferencesKey.IS_LOGGED_IN, false)
                save(PreferencesKey.IS_BUILD_ROADMAP, false)
            }
        }
    }

}
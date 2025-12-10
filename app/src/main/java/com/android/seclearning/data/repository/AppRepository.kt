package com.android.seclearning.data.repository

import com.android.seclearning.common.utils.backgroundLaunch
import com.android.seclearning.data.database.AppDatabase
import com.android.seclearning.data.local.PreferencesKey
import com.android.seclearning.data.local.PreferencesManager
import dagger.Lazy
import java.util.Locale
import javax.inject.Inject

class AppRepository @Inject constructor(
    val preferencesManager: Lazy<PreferencesManager>
//    private val appDatabase: Lazy<AppDatabase>
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
        backgroundLaunch {
            preferencesManager.get().save(PreferencesKey.IS_LOGGED_IN, true)
        }
    }

    fun setBuildRoadmap() {
        backgroundLaunch {
            preferencesManager.get().save(PreferencesKey.IS_BUILD_ROADMAP, true)
        }
    }

    fun checkAppStatus() {
        mIsFirstOpenApp =
            preferencesManager.get().getBoolean(PreferencesKey.IS_FIRST_TIME_OPEN_APP, true)
        mIsLoggedIn =
            preferencesManager.get().getBoolean(PreferencesKey.IS_LOGGED_IN, false)
        mIsBuildRoadmap =
            preferencesManager.get().getBoolean(PreferencesKey.IS_BUILD_ROADMAP, false)
    }

    fun getLanguageSet(): String = preferencesManager.get().getString(PreferencesKey.KEY_LANGUAGE)
        ?: Locale.getDefault().language

    fun getCountryCode() =
        preferencesManager.get().getString(PreferencesKey.COUNTRY_CODE) ?: ""

}
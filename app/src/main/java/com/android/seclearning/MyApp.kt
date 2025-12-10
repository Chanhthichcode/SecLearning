package com.android.seclearning

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.android.seclearning.common.font.FontPreloader
import com.android.seclearning.common.utils.backgroundLaunch
import com.android.seclearning.data.repository.AppRepository
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : MultiDexApplication(), DefaultLifecycleObserver{
    var fontPreloader = FontPreloader(this)

    @Inject
    lateinit var appRepository: Lazy<AppRepository>

    override fun onCreate() {
        super<MultiDexApplication>.onCreate()
        instance = this
        Logger.init()
        AppConfig.detectAppLanguage()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        backgroundLaunch {
            preloadFonts()
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        AppConfig.isAppInForeground = true
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        AppConfig.isAppInForeground = false
    }

    companion object {
        lateinit var instance: MyApp
        fun getContext(): Context = instance.applicationContext
    }

    private fun preloadFonts() {
        fontPreloader.run {
            preloadFont(R.font.my_font_regular)
            preloadFont(R.font.my_font_medium)
            preloadFont(R.font.my_font_semibold)
            preloadFont(R.font.my_font_bold)
        }
    }
}

fun myApp() = MyApp.instance
fun appContext() = MyApp.getContext()
fun appRepository(): AppRepository = MyApp.instance.appRepository.get()

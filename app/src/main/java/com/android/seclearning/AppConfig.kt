package com.android.seclearning

import android.app.ActivityManager
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import com.android.seclearning.common.utils.isLowRamDevice
import java.util.Locale


object AppConfig {
    var isAppInForeground = false
    var appLanguage: String = ""

    private fun getDeviceRAM(context: Context): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem
    }

    private fun isXhdpiDevice(context: Context) =
        context.resources.displayMetrics.densityDpi == DisplayMetrics.DENSITY_XHIGH

    private fun isCortexA53() = false

    fun isSmallRamDevice(context: Context): Boolean {
        val _4GbRAM = 4 * 1024 * 1024 * 1024L
        return getDeviceRAM(context) <= _4GbRAM || isLowRamDevice(context)
    }

    fun isLowPerformanceDevice(context: Context): Boolean {
        val _4GbRAM = 4 * 1024 * 1024 * 1024L
        val isAndroid11OrLower = Build.VERSION.SDK_INT <= Build.VERSION_CODES.R
        return (isAndroid11OrLower || getDeviceRAM(context) <= _4GbRAM)
                || isLowRamDevice(context) || isCortexA53()
    }

    fun canRunAnim(context: Context): Boolean {
        val isAndroid10OrLower = Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q
        return !isLowPerformanceDevice(context) && !isAnimationDisabledByUser(context)
                && !isAndroid10OrLower && !isXhdpiDevice(context)
    }

    private fun canRunHeavyAnim(context: Context): Boolean {
        return !isLowPerformanceDevice(context) && !isAnimationDisabledByUser(context)
    }

    fun canRunPreviewAnim(context: Context): Boolean = canRunHeavyAnim(context)

    fun canRunDetailAnim(context: Context): Boolean = false

    fun canRunAnimation(context: Context): Boolean = canRunHeavyAnim(context)

    fun reduceAnimation(context: Context): Boolean {
        val isAndroid10OrLower = Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q
        return isSmallRamDevice(context) || isAndroid10OrLower
                || isAnimationDisabledByUser(context) || isXhdpiDevice(context) || isCortexA53()
    }

    fun getLocalizedContext(context: Context): Context {
//        val config = context.resources.configuration
//        config.setLocale(locale)
//        Locale.setDefault(locale)
//        return context.createConfigurationContext(config)
        if (appLanguage.isNotEmpty()) {
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            configuration.locale = Locale(appLanguage)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        return ContextWrapper(context)
    }

    fun detectAppLanguage() {
        appLanguage = appRepository().getLanguageSet()
        if (appLanguage.isNotEmpty()) {
            Locale.setDefault(Locale(appLanguage))
            getLocalizedContext(appContext())
        }
    }

    fun isAnimationDisabledByUser(context: Context): Boolean {
        return try {
            val scale = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1.0f
            )
            scale == 0.0f
        } catch (e: Settings.SettingNotFoundException) {
            false
        }
    }
}
package com.android.seclearning.common.utils

import android.app.ActivityManager
import androidx.core.content.ContextCompat
import com.android.seclearning.appContext
import kotlin.jvm.java

/**
 * Get system services
 */
inline fun <reified T> getSystemService(): T? =
    ContextCompat.getSystemService(appContext(), T::class.java)

val activityManager get() = getSystemService<ActivityManager>()
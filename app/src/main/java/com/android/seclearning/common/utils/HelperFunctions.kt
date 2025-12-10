package com.android.seclearning.common.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.BatteryManager
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.view.View
import androidx.core.os.postDelayed

fun postDelayedSkipException(delay: Long = 0, task: () -> Unit): Runnable {
    return Handler(Looper.getMainLooper()).postDelayed(delay) {
        runCatchException(task) { e -> e.printStackTrace() }
    }
}

fun <T> runCatchException(block: () -> T, catchBlock: (Exception) -> Unit = {}): T? {
    try {
        return block.invoke()
    } catch (e: Exception) {
        catchBlock(e)
    } catch (e: java.lang.IllegalStateException) {
        catchBlock(e)
    } catch (e: java.lang.Exception) {
        catchBlock(e)
    } catch (e: IllegalStateException) {
        catchBlock(e)
    } catch (e: OutOfMemoryError) {
        catchBlock(kotlin.Exception("Out of memory!"))
    }
    return null
}

@SuppressLint("ClickableViewAccessibility")
fun enableTouch(view: View, isEnable: Boolean, revertAfter: Long = 0L) {
    if (isEnable)
        view.setOnTouchListener { _, _ -> false }
    else
        view.setOnTouchListener { _, _ -> true }
    if (revertAfter > 0) {
        postDelayedSkipException(revertAfter) {
            enableTouch(view, !isEnable)
        }
    }
}

fun isLowRamDevice(context: Context): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return activityManager.isLowRamDevice
}
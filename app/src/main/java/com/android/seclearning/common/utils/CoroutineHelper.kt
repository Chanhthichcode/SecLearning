package com.android.seclearning.common.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

fun getMainScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Main)
}

fun mainLaunch(block: suspend CoroutineScope.() -> Unit) = getMainScope().launch { block() }

fun mainLaunchSafe(block: suspend CoroutineScope.() -> Unit) = getMainScope().launch {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun backgroundLaunch(block: suspend CoroutineScope.() -> Unit) =
    getMainScope().launch(Dispatchers.Default) { block() }

fun backgroundLaunchSafe(block: suspend CoroutineScope.() -> Unit) =
    getMainScope().launch(Dispatchers.Default) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

fun AndroidViewModel.launch(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch { block() }

fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch { block() }

fun Fragment.launch(block: suspend CoroutineScope.() -> Unit) =
    lifecycleScope.launch { block() }

suspend fun <T> runInBackground(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.Default) { block() }

suspend fun <T> runInIO(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.IO) { block() }

suspend fun <T> runInMain(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.Main) { block() }

fun <T> Continuation<T>.safeResume(value: T, resumed: AtomicBoolean) {
    if (resumed.compareAndSet(false, true)) {
        resume(value)
    }
}
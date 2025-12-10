package com.android.seclearning.common.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import androidx.lifecycle.LiveData
import kotlin.let

class MyInternet(private val context: Context, private val standAlone: Boolean = false) :
    LiveData<Boolean?>(null) {
    companion object {
        private var isNetworkConnected = true
    }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val handler = Handler(Looper.getMainLooper())
    private var delayRunnable: Runnable? = null

    override fun onActive() {
        super.onActive()
        registerNetworkCallback()
    }

    override fun onInactive() {
        super.onInactive()
        cancelPendingStateUpdate()
        unregisterNetworkCallback()
    }

    private fun registerNetworkCallback() {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                updateNetworkState()
            }

            override fun onLost(network: Network) {
                updateNetworkState()
            }

            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, capabilities)
                updateNetworkState()
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        updateNetworkState()
    }

    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun updateNetworkState() {
        val isConnected = NetworkUtils.isConnected()
        if (!isConnected) {
            startDelayToPostFalse()
        } else {
            cancelPendingStateUpdate()
            myPostValue()
        }
    }

    private fun startDelayToPostFalse() {
        cancelPendingStateUpdate()
        delayRunnable = Runnable {
            myPostValue()
        }
        handler.postDelayed(delayRunnable!!, 2000)
    }

    private fun cancelPendingStateUpdate() {
        delayRunnable?.let { handler.removeCallbacks(it) }
    }

    private fun myPostValue() {
        val newValue = NetworkUtils.isConnected()
        if (standAlone) {
            isNetworkConnected = newValue
            if (newValue != value) {
                postValue(newValue)
            }
            return
        }
        if (isNetworkConnected != newValue) {
            isNetworkConnected = newValue
            postValue(newValue)
        }
    }

    fun postDelayedSkipException(delay: Long = 0, task: () -> Unit): Runnable {
        return Handler(Looper.getMainLooper()).postDelayed(delay) {
            runCatchException(task) { e -> e.printStackTrace() }
        }
    }
}
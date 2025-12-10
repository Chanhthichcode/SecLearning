package com.android.seclearning.common.utils

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import androidx.annotation.RequiresPermission
import com.android.seclearning.appContext


object NetworkUtils {

    fun openNetworkSetting(context: Context) {
        context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }

    @SuppressLint("InlinedApi")
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isConnected(): Boolean {
        val connectivityManager =
            appContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
                && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_SATELLITE)
                )
    }
}
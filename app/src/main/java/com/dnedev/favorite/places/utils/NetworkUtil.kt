package com.dnedev.favorite.places.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager: ConnectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        connectivityManager.allNetworks.map {
            connectivityManager.getNetworkInfo(it) to connectivityManager.getNetworkCapabilities(
                it
            )
        }
            .firstOrNull {
                it.first?.isConnected ?: false
                        && it.second?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
                        && it.second?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
            } != null
    } else {
        connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}
package com.diplomproject.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import org.koin.java.KoinJavaComponent.getKoin

class OnlineRepository : LiveData<Boolean>() {

    private val context: Context = getKoin().get()
    private val availableNetworks = mutableSetOf<Network>()
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val request: NetworkRequest = NetworkRequest.Builder().build()
    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            availableNetworks.remove(network)
            update(availableNetworks.isNotEmpty())
        }

        override fun onAvailable(network: Network) {
            availableNetworks.add(network)
            update(availableNetworks.isNotEmpty())
        }
    }



    override fun onActive() {
        connectivityManager.registerNetworkCallback(request, callback)
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(callback)
    }

    private fun update(online: Boolean) {
        if (online != value) {
            postValue(online)
        }
    }

    fun currentStatus(): Boolean {
        onActive()
        val hasNetwork = availableNetworks.isNotEmpty()
        postValue(hasNetwork)
        return hasNetwork
    }
}
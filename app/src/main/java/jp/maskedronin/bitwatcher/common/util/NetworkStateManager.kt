package jp.maskedronin.bitwatcher.common.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import javax.inject.Singleton

private val networkCapabilities: Set<Int> = setOf(
    NetworkCapabilities.NET_CAPABILITY_INTERNET,
    NetworkCapabilities.NET_CAPABILITY_VALIDATED
)

@Singleton
class NetworkStateManager @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {
    private val channel = BroadcastChannel<Boolean>(Channel.CONFLATED)
        .apply { offer(false) }
    val isConnected: Flow<Boolean> = channel.asFlow()

    init {
        setNetworkCallback()
    }

    /**
     * https://takasfz.hatenablog.com/entry/2019/07/04/191744
     */
    private fun setNetworkCallback() {
        val networkRequest: NetworkRequest = NetworkRequest.Builder()
            .also { builder ->
                networkCapabilities.forEach { capability ->
                    builder.addCapability(capability)
                }
            }
            .build()

        connectivityManager.registerNetworkCallback(
            networkRequest,
            NetworkStateChangedCallback(
                connectivityManager,
                onChanged = { isConnected ->
                    channel.offer(isConnected)
                }
            )
        )
    }
}

private class NetworkStateChangedCallback(
    private val connectivityManager: ConnectivityManager,
    private val onChanged: (Boolean) -> Unit
) : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network?) {
        onChanged(connectivityManager.isConnected())
    }

    override fun onLost(network: Network?) {
        onChanged(connectivityManager.isConnected())
    }
}

fun ConnectivityManager.isConnected(): Boolean {
    val activeNetworks = allNetworks
        .mapNotNull { network ->
            getNetworkCapabilities(network)
        }
        .filter { capabilities ->
            networkCapabilities.all { capability ->
                capabilities.hasCapability(capability)
            }
        }
    return activeNetworks.isNotEmpty()
}

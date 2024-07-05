package fr.ec.pmr24.tea1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat.getSystemService
import fr.ec.pmr24.tea1.data.sql.sync
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager = getSystemService(context, ConnectivityManager::class.java)
        if (connectivityManager != null) {
            val currentNetwork = connectivityManager.activeNetwork
            val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
            val isNetworkAvailable =
                caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

            if (isNetworkAvailable) {
                GlobalScope.launch {
                    sync.items(context)
                }
            }
        }
    }
}
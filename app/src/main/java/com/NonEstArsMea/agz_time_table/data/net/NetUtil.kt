package com.NonEstArsMea.agz_time_table.data.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.NetUtil
import javax.inject.Singleton


object NetUtilImpl: NetUtil {


    override fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            }
        }
        return false
    }

}
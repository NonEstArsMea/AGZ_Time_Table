package com.NonEstArsMea.agz_time_table.data.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.NetUtil
import javax.inject.Inject


class NetUtilImpl @Inject constructor(
    val context: Context
): NetUtil {

    private var netConnection : MutableLiveData<Boolean> = MutableLiveData(false)
    override fun checkNetConn() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                netConnection.value = true
                return
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                netConnection.value = true
                return
            }
        }
        netConnection.value = false
    }

    override fun isNetConnection(): Boolean {
        return netConnection.value!!
    }

    override fun getNetLiveData(): MutableLiveData<Boolean>{
        return netConnection
    }

    override fun setNetConnection(isConnection: Boolean){
        netConnection.postValue(isConnection)
    }

}
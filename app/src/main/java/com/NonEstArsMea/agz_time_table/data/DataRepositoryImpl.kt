package com.NonEstArsMea.agz_time_table.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.DataRepository
import java.net.URL

object DataRepositoryImpl: DataRepository {

    private var dataLiveData = MutableLiveData<String>()

    private var content = ""

    override suspend fun loadData(): MutableLiveData<String> {
        if(dataLiveData.value == null){
            val connection = URL("http://a0755299.xsph.ru/wp-content/uploads/3-1-1.txt").openConnection()
            connection.connect()
            content = connection.getInputStream().bufferedReader().use { it.readText() }
            dataLiveData.postValue(content)
        }
        return dataLiveData
    }

    override fun getData(): MutableLiveData<String>{
        return dataLiveData
    }

    override fun getContent():String{
        return content
    }

    override fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if(capabilities != null){
                if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                    return true
                }else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    return true
                }
            }
            return false
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

}
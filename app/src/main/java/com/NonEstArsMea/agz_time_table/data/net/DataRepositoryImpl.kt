package com.NonEstArsMea.agz_time_table.data.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepositoryImpl @Inject constructor(
    private val context: Context
) : DataRepository {

    private var _dataLiveData = MutableLiveData<String>()
    private var content = ""
    private var dataIsLoad =  MutableLiveData<Boolean>()

    override suspend fun loadData(): LiveData<String> {
        if (_dataLiveData.value == null) {
            withContext(Dispatchers.IO) {
                val connection =
                    URL("http://a0755299.xsph.ru/data/combined_output.txt").openConnection()
                Log.e("cont", connection.toString())
                connection.connect()
                content = connection.getInputStream().bufferedReader().use { it.readText() }
                _dataLiveData.postValue(content)
                dataIsLoad.postValue(true)
            }
        }
        return _dataLiveData
    }

    override fun dataIsLoad(): LiveData<Boolean> {
        return dataIsLoad
    }

    override fun getData(): LiveData<String> {
        return _dataLiveData
    }

    override fun getContent(): String {
        return content
    }

    override fun isInternetConnected(): Boolean {
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
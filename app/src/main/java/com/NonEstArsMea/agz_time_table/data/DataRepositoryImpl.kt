package com.NonEstArsMea.agz_time_table.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(private val context: Context) : DataRepository {

    private var _dataLiveData = MutableLiveData<String>()
    private val dataLiveData: LiveData<String>
        get() = _dataLiveData

    private var content = ""

    override suspend fun loadData(): LiveData<String> {
        if (_dataLiveData.value == null) {
            withContext(Dispatchers.IO) {
                val connection =
                    URL("http://a0755299.xsph.ru/wp-content/uploads/3-1-1.txt").openConnection()
                connection.connect()
                content = connection.getInputStream().bufferedReader().use { it.readText() }
                _dataLiveData.postValue(content)
            }
        }
        return dataLiveData
    }

    override fun getData(): LiveData<String> {
        return dataLiveData
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
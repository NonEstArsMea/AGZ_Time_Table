package com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface DataRepository {

    suspend fun loadData(): LiveData<String>

    fun getData(): LiveData<String>

    fun getContent(): String

    fun isInternetConnected(): Boolean
}
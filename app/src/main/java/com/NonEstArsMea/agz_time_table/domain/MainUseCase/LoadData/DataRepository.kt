package com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData

import android.content.Context
import androidx.lifecycle.MutableLiveData

interface DataRepository {

    suspend fun loadData(): MutableLiveData<String>

    fun getData(): MutableLiveData<String>

    fun getContent(): String

    fun isInternetConnected(context: Context): Boolean
}
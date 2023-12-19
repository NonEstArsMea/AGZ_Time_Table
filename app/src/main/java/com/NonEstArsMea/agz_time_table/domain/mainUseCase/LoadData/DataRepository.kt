package com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData

import androidx.lifecycle.LiveData

interface DataRepository {

    suspend fun loadData(): LiveData<String>

    fun getData(): LiveData<String>

    fun getContent(): String

    fun isInternetConnected(): Boolean

    fun dataIsLoad(): LiveData<Boolean>
}
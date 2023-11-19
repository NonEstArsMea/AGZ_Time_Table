package com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface DataRepository {
    suspend fun loadData(): MutableLiveData<String>
}
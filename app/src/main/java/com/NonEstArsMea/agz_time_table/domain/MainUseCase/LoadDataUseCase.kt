package com.NonEstArsMea.agz_time_table.domain.MainUseCase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoadDataUseCase(private val repository: DataRepository) {
    suspend fun execute(): MutableLiveData<String> {
        return repository.loadData()
    }
}
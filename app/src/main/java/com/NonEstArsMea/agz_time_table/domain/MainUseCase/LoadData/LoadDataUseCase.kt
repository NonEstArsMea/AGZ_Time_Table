package com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData

import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.DataRepository
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(private val repository: DataRepository) {
    suspend fun execute(): MutableLiveData<String> {
        return repository.loadData()
    }
}
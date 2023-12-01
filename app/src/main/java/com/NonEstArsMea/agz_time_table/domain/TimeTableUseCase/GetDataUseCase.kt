package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.DataRepository
import javax.inject.Inject

class GetDataUseCase @Inject constructor(private val repository: DataRepository) {
    fun execute(): MutableLiveData<String>{
        return repository.getData()
    }
}
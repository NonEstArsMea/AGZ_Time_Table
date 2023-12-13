package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import android.util.Log
import androidx.lifecycle.LiveData
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.DataRepository
import javax.inject.Inject

class GetDataUseCase @Inject constructor(private val repository: DataRepository) {
    fun execute(): LiveData<String> {
        Log.e("fin", "getLD")
        return repository.getData()
    }
}
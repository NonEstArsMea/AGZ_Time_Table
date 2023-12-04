package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import javax.inject.Inject

class GetWeekTimeTableListUseCase @Inject constructor(
    private val timeTableRepository: TimeTableRepository,
    private val context: Context
) {
    suspend fun execute(): List<List<CellApi>> {
        return timeTableRepository.getWeekTimeTable(context)
    }

    fun getArrayOfWeekTimeTable():MutableLiveData<List<List<CellApi>>>{
        return timeTableRepository.getArrayOfWeekTimeTable()
    }
}
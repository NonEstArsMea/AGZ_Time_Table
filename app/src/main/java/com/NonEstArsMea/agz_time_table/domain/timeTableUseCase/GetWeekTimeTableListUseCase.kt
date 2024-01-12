package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import javax.inject.Inject

class GetWeekTimeTableListUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository
) {
    suspend fun execute(): List<List<CellClass>> {
        return timeTableRepositoryImpl.getWeekTimeTable()
    }

    fun getArrayOfWeekTimeTable(): MutableLiveData<List<List<CellClass>>> {
        return timeTableRepositoryImpl.getArrayOfWeekTimeTable()
    }
}
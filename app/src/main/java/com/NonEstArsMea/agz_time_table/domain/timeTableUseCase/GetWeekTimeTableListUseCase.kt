package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import javax.inject.Inject

class GetWeekTimeTableListUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository
) {
    suspend fun execute(): List<List<CellApi>> {
        return timeTableRepositoryImpl.getWeekTimeTable()
    }

    fun getArrayOfWeekTimeTable(): MutableLiveData<List<List<CellApi>>> {
        Log.e("tag_uc", timeTableRepositoryImpl.getArrayOfWeekTimeTable().value.toString())
        return timeTableRepositoryImpl.getArrayOfWeekTimeTable()
    }
}
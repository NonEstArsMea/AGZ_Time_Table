package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import javax.inject.Inject

class GetWeekTimeTableListUseCase @Inject constructor(
    private val context: Context,
    private val timeTableRepositoryImpl: TimeTableRepository
) {
    suspend fun execute(): List<List<CellApi>> {
        return timeTableRepositoryImpl.getWeekTimeTable(context)
    }

    fun getArrayOfWeekTimeTable(): MutableLiveData<List<List<CellApi>>> {
        return timeTableRepositoryImpl.getArrayOfWeekTimeTable()
    }
}
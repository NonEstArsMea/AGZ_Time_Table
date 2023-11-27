package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

class GetWeekTimeTableListUseCase(
    private val timeTableRepository: TimeTableRepository,
    private val context: Context
) {
    suspend fun execute(newData: String): List<List<CellApi>> {
        return timeTableRepository.getWeekTimeTable(newData, context)
    }
}
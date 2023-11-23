package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

class GetWeekTimeTableListUseCase(private val timeTableRepository: TimeTableRepository) {
    suspend fun execute(newData: String):List<List<CellApi>>{
        return timeTableRepository.getWeekTimeTable(newData)
    }
}
package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

class GetWeekTimeTableListUseCase(private val timeTableRepository: TimeTableRepository) {
    suspend fun execute(newData: String, dayOfWeek: ArrayList<String>, mainParam: String):ArrayList<ArrayList<CellApi>>{
        return timeTableRepository.getWeekTimeTable(newData, dayOfWeek, mainParam)
    }
}
package com.NonEstArsMea.agz_time_table.domain.useCases

import com.NonEstArsMea.agz_time_table.domain.TimeTableRepository
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

class GetDayTimeTableUseCase(private val timeTableRepository: TimeTableRepository) {
    fun getDayTimeTable(dayOfWeek: Int):ArrayList<CellApi> {
        return timeTableRepository.getDayTimeTable(dayOfWeek)
    }

}
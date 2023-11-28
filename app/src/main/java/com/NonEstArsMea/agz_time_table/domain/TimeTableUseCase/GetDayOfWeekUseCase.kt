package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.DateRepository

class GetDayOfWeekUseCase(private val repository: DateRepository) {
    fun execute(): Int{
        return repository.getDayOfWeek()
    }
}
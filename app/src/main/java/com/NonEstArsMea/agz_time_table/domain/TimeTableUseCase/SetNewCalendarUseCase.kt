package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.DateRepository
import java.util.Calendar

class SetNewCalendarUseCase(private val repository: DateRepository) {
    fun execute(newTime: Int?){
        if(newTime != null){
            repository.setNewCalendar(newTime)
        }
    }
}
package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.DateRepository
import java.util.Calendar
import javax.inject.Inject

class SetNewCalendarUseCase @Inject constructor(private val repository: DateRepository) {
    fun execute(newTime: Int?){
        if(newTime != null){
            repository.setNewCalendar(newTime)
        }
    }
}
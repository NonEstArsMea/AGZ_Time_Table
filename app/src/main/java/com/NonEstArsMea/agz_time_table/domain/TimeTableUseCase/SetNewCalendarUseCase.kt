package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.DateRepository
import java.util.Calendar
import javax.inject.Inject

class SetNewCalendarUseCase @Inject constructor() {
    fun execute(newTime: Int?){
        if(newTime != null){
            DateRepositoryImpl.setNewCalendar(newTime)
        }
    }
}
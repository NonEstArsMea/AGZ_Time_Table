package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import javax.inject.Inject

class SetNewCalendarUseCase @Inject constructor() {
    fun execute(newTime: Int?){
        if(newTime != null){
            DateRepositoryImpl.setNewCalendar(newTime)
        }
    }
}
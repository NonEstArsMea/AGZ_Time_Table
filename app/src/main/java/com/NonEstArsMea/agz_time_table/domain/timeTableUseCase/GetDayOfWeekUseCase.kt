package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import javax.inject.Inject

class GetDayOfWeekUseCase @Inject constructor() {
    fun execute(): Int{
        return DateRepositoryImpl.getDayOfWeek()
    }
}
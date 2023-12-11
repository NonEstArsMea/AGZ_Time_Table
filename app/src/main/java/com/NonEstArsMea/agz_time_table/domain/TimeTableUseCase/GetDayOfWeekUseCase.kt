package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.DateRepository
import javax.inject.Inject

class GetDayOfWeekUseCase @Inject constructor() {
    fun execute(): Int{
        return DateRepositoryImpl.getDayOfWeek()
    }
}
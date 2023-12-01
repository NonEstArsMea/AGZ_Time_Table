package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.DateRepository
import javax.inject.Inject

class GetDayOfWeekUseCase @Inject constructor(private val repository: DateRepository) {
    fun execute(): Int{
        return repository.getDayOfWeek()
    }
}
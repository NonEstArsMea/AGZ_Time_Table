package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.domain.DateRepository

class GetDateUseCase(private val repository: DateRepository, private val context: Context) {
    fun execute(day: Int, month: Int, year: Int):String{
        return repository.getStrDate(day, month, year, context)
    }
}
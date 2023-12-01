package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.domain.DateRepository
import javax.inject.Inject

class GetDateUseCase @Inject constructor(private val repository: DateRepository, private val context: Context) {
    fun execute(day: Int, month: Int, year: Int):String{
        return repository.getStrDate(day, month, year, context)
    }
}
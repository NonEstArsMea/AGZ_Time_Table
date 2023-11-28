package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.DateRepository

class GetMonthUseCase(private val repository: DateRepository, private val context: Context) {
    fun execute(): String{
        return repository.monthAndDayNow(context)
    }
}
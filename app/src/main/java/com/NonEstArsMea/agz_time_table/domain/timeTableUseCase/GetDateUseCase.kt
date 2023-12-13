package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import javax.inject.Inject

class GetDateUseCase @Inject constructor(private val context: Context) {
    fun execute(day: Int, month: Int, year: Int):String{
        return "$day ${context.getString(DateRepositoryImpl.getMonth(month))} - $year"
    }
}
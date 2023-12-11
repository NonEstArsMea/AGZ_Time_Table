package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.DateRepository
import javax.inject.Inject

class GetMonthUseCase @Inject constructor(private val context: Context) {
    fun execute(): String{
        return DateRepositoryImpl.monthAndDayNow(context)
    }
}
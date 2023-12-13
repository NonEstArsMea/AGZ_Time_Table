package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import javax.inject.Inject

class GetMonthUseCase @Inject constructor(private val context: Context) {
    fun execute(): String{
        return DateRepositoryImpl.monthAndDayNow(context)
    }
}
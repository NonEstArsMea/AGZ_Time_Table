package com.NonEstArsMea.agz_time_table.domain

import android.content.Context

interface DateRepository {

    fun getMonth(number: Int): Int

    fun monthAndDayNow(context: Context): String

    fun engToRusDayOfWeekNumbers(time: Int): Int

    fun dayNumberOnButton(): List<String>

    fun setNewCalendar(newTime:Int = 0)

    fun getArrayOfWeekDate(): List<String>

    fun getDayOfWeek(): Int

    fun setDayNow()

}
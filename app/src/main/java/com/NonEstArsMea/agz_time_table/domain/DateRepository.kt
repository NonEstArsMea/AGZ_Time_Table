package com.NonEstArsMea.agz_time_table.domain

interface DateRepository {

    fun monthToString(number: Int): String

    fun monthAndDayNow(): String

    fun engToRusDayOfWeekNumbers(time: Int): Int

    fun dayNumberOnButton(): List<String>

    fun setNewCalendar(newTime:Int = 0)

    fun getArrayOfWeekDate(): List<String>

    fun getDayOfWeek(): Int
}
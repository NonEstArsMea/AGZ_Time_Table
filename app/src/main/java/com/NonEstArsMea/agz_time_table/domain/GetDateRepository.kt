package com.NonEstArsMea.agz_time_table.domain

import java.util.Calendar

interface GetDateRepository {

    fun monthToString(number: Int): String

    fun monthNumber(number: Int): String

    fun convertDateToText(date: Calendar): String

    fun monthAndDayNow(): String

    fun engToRusDayOfWeekNumbers(time: Int): Int

    fun dayNumberOnButton(): List<String>

    fun updateCalendar(newTime:Int = 0): Calendar

    fun getArrayOfWeekDate(): List<String>

    fun getDayOfWeek(): Int
}
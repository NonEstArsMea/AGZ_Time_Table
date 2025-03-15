package com.NonEstArsMea.agz_time_table.util

import android.content.Context
import com.NonEstArsMea.agz_time_table.R
import java.util.Calendar

object DateManager {

    private var calendar: Calendar = Calendar.getInstance()

    fun setDayNow() {
        calendar = Calendar.getInstance()
        val dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
        if (dayOfWeek == SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    fun getMonth(number: Int): Int {
        val month = arrayOf(
            R.string.January, R.string.February,
            R.string.March, R.string.April, R.string.May, R.string.June,
            R.string.July, R.string.August, R.string.September,
            R.string.October, R.string.November, R.string.December
        )
        return month[number]
    }

    fun getMonthNominativeСase(number: Int): String {
        val month = arrayOf(
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь",

            )
        return month[number]
    }

    fun getMonthNominativeСaseSecondVariant(number: String): String {
        val intNumber = if (number[0] == '0') {
            number[1].toString().toInt()
        } else {
            number.toInt()
        }
        val month = arrayOf(
            "Января",
            "Февраля",
            "Марта",
            "Апреля",
            "Мая",
            "Июня",
            "Июля",
            "Августа",
            "Сентября",
            "Октября",
            "Ноября",
            "Декабря",

            )
        return month[intNumber - 1]
    }

    fun getMonthNominativeСaseByString(number: String): String {
        val intNumber = if (number[0] == '0') {
            number[1].toString().toInt()
        } else {
            number.toInt()
        }
        val month = arrayOf(
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь",

            )
        return month[intNumber - 1]
    }

    fun dayNumberOnButton(): List<String> {
        val days = mutableListOf<String>()

        val razn = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
        calendar.add(Calendar.DAY_OF_MONTH, -razn)

        for (a in 0..5) {
            days.add(calendar.get(Calendar.DAY_OF_MONTH).toString())
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendar.add(Calendar.DAY_OF_WEEK, razn - 6)

        return days
    }

    fun monthAndDayNow(context: Context): String {

        val dayNow = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val monthNow = calendar.get(Calendar.MONTH)
        val monthStrNow = getMonthNominativeСase(monthNow)
        val yearNow = calendar.get(Calendar.YEAR).toString()

        return "$monthStrNow - $yearNow"
    }

    fun getDateString(context: Context, s: String): String {
        val a = s.split("-")
        return "${a[0]} ${context.getString(getMonth(a[1].toInt() - 1))} - ${a[2]}"
    }


    fun setNewCalendar(newTime: Int) {
        calendar.add(Calendar.DAY_OF_MONTH, newTime)
    }


    fun engToRusDayOfWeekNumbers(time: Int): Int {
        return if (time == 1) {
            SUNDAY
        } else {
            (time - 2)
        }
    }

    fun getArrayOfWeekDate(): ArrayList<String> {
        val days = ArrayList<String>()

        val razn = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
        calendar.add(Calendar.DAY_OF_MONTH, -razn)

        var dayNow: String
        var monthNow: String
        var yearNow: String

        for (a in 0..5) {
            dayNow = calendar.get(Calendar.DAY_OF_MONTH).toString()
            monthNow = if (calendar.get(Calendar.MONTH) + 1 < 10)
                "0${calendar.get(Calendar.MONTH) + 1}"
            else
                (calendar.get(Calendar.MONTH) + 1).toString()

            yearNow = calendar.get(Calendar.YEAR).toString()

            days.add("${dayNow}-${monthNow}-${yearNow}")
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendar.add(Calendar.DAY_OF_WEEK, razn - 6)
        return days
    }

    fun getDayOfWeek(): Int {
        var dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
        if (dayOfWeek == SUNDAY) {
            dayOfWeek = 0
        }
        return dayOfWeek
    }

    // Возвращает строку с датой конца и начала
    fun getWeekDateText(context: Context): String {
        var text = ""
        val razn = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
        calendar.add(Calendar.DAY_OF_MONTH, -razn)

        text += calendar.get(Calendar.DAY_OF_MONTH).toString() + " " + context.getString(
            getMonth(
                calendar.get(Calendar.MONTH)
            )
        ) + " - "
        calendar.add(Calendar.DAY_OF_MONTH, 5)
        text += calendar.get(Calendar.DAY_OF_MONTH).toString() + " " + context.getString(
            getMonth(
                calendar.get(Calendar.MONTH)
            )
        )

        calendar.add(Calendar.DAY_OF_WEEK, razn - 5)
        return text
    }

    fun getDateList(): List<String> {

        val razn = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
        calendar.add(Calendar.DAY_OF_MONTH, -razn)

        val list = buildList {
            (1..6).forEach {
                this.add(calendar.get(Calendar.DAY_OF_MONTH).toString() + "\n")
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        calendar.add(Calendar.DAY_OF_WEEK, razn - 6)

        return list
    }

    fun getFullDateNow(dayOffset: Int): String {

        setDayNow()
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset)
        val dayNow = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val monthNow = "0${calendar.get(Calendar.MONTH) + 1}"
        val yearNow = calendar.get(Calendar.YEAR).toString()
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset * (-1))

        return "$dayNow-$monthNow-$yearNow"
    }

    fun getDayAndMonth(date: String):String{
        val dateList = date.split("-")
        return dateList[0] + " " + getMonthNominativeСaseSecondVariant(dateList[1])
    }

    private const val SUNDAY = 6


}
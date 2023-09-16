package com.NonEstArsMea.agz_time_table.domain

import java.util.Calendar

class GetDateRepositoryImpl(calendar: Calendar): GetDateRepository {

    var calendar = calendar

    override fun monthToString(number: Int): String {
        val month = arrayOf(
            "Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
            "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"
        )
        return month[number]
    }

    override fun monthNumber(number: Int): String {
        TODO("Not yet implemented")
    }

    override fun convertDateToText(date: Calendar): String {
        TODO("Not yet implemented")
    }

    override fun dayNumberOnButton(): List<String> {
        var days = mutableListOf<String>()

        var razn = engToRusDayOfWeekNumbers(calendar.get(Calendar.DAY_OF_WEEK)) - 1
        calendar.add(Calendar.DAY_OF_MONTH,- razn)

        for(a in 0..5){
            days.add(calendar.get(Calendar.DAY_OF_MONTH).toString())
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        calendar.add(Calendar.DAY_OF_WEEK, razn - 6)

        return days
    }

    override fun monthAndDayNow(): String {

        val dayNow = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val monthNow  = calendar.get(Calendar.MONTH)
        val monthStrNow = monthToString(monthNow)
        val yearNow = calendar.get(Calendar.YEAR).toString()

        return "$dayNow $monthStrNow - $yearNow"
    }

    override fun updateCalendar(newTime: Int): Calendar {
        calendar.add(Calendar.DAY_OF_MONTH, newTime)
        //Log.e("my_tag", calendar.time.toString())
        return calendar
    }

    override fun engToRusDayOfWeekNumbers(time: Int): Int {
        if(time == 1){
            return 7
        }else{
            return (time - 1)
        }
    }

    override fun getArrayOfWeekDate(): ArrayList<String> {
        var days = ArrayList<String>()

        var razn = engToRusDayOfWeekNumbers(calendar.get(Calendar.DAY_OF_WEEK)) - 1
        calendar.add(Calendar.DAY_OF_MONTH,- razn)

        var dayNow = ""
        var monthNow = ""
        var yearNow = ""

        for(a in 0..5){
            dayNow = calendar.get(Calendar.DAY_OF_MONTH).toString()
            monthNow  = if(calendar.get(Calendar.MONTH) < 10)
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

    override fun getDayOfWeek(): Int {
        if(calendar.get(Calendar.DAY_OF_WEEK) == 1){
            return 1
        }else{
            return calendar.get(Calendar.DAY_OF_WEEK) - 2
        }
    }
}
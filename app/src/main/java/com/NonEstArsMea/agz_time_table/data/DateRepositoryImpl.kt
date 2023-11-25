package com.NonEstArsMea.agz_time_table.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.DateRepository
import java.util.Calendar

object DateRepositoryImpl: DateRepository {

    private var calendar:Calendar = Calendar.getInstance()

    private val constCalendar:Calendar = Calendar.getInstance()

    private val calendarLiveData = MutableLiveData<Calendar>()

    fun setDayNow(){
        calendar = constCalendar
        updateCalendar()
    }

    override fun monthToString(number: Int): String {
        val month = arrayOf(
            "Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
            "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"
        )
        return month[number]
    }

    override fun dayNumberOnButton(): List<String> {
        val days = mutableListOf<String>()

        val razn = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
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

    override fun setNewCalendar(newTime: Int){
        calendar.add(Calendar.DAY_OF_MONTH, newTime)
        updateCalendar()
    }
    fun updateCalendar(){
        calendarLiveData.value = calendar
    }

    fun getCalendarLD(): MutableLiveData<Calendar>{
        return calendarLiveData
    }

    override fun engToRusDayOfWeekNumbers(time: Int): Int {
        if(time == 1){
            return 7
        }else{
            return (time - 2)
        }
    }

    override fun getArrayOfWeekDate(): ArrayList<String> {
        val days = ArrayList<String>()

        val razn = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
        calendar.add(Calendar.DAY_OF_MONTH,-razn)

        var dayNow = ""
        var monthNow = ""
        var yearNow = ""

        for(a in 0..5){
            dayNow = calendar.get(Calendar.DAY_OF_MONTH).toString()
            monthNow  = if(calendar.get(Calendar.MONTH)+1 < 10)
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
        val dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
        if(dayOfWeek == 7){
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            return 1
        }
        return dayOfWeek
    }


}
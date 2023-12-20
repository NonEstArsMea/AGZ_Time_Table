package com.NonEstArsMea.agz_time_table.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.DateRepository
import java.util.Calendar

object DateRepositoryImpl: DateRepository {

    private var calendar:Calendar = Calendar.getInstance()


    private val calendarLiveData = MutableLiveData<Calendar>()

    override fun setDayNow(){
        calendar = Calendar.getInstance()
        updateCalendar()
    }

    override fun getMonth(number: Int): Int {
        val month = arrayOf(
            R.string.January, R.string.February,
            R.string.March, R.string.April, R.string.May, R.string.June,
            R.string.July, R.string.August, R.string.September,
            R.string.October, R.string.November, R.string.December
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

    override fun monthAndDayNow(context: Context): String {

        val dayNow = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val monthNow  = calendar.get(Calendar.MONTH)
        val monthStrNow = context.getString(getMonth(monthNow))
        val yearNow = calendar.get(Calendar.YEAR).toString()

        return "$dayNow $monthStrNow - $yearNow"
    }


    override fun setNewCalendar(newTime: Int){
        calendar.add(Calendar.DAY_OF_MONTH, newTime)
        updateCalendar()
    }
    private fun updateCalendar(){
        calendarLiveData.value = calendar
    }


    override fun engToRusDayOfWeekNumbers(time: Int): Int {
        return if(time == 1){
            SUNDAY
        }else{
            (time - 2)
        }
    }

    override fun getArrayOfWeekDate(): ArrayList<String> {
        val days = ArrayList<String>()

        val razn = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
        calendar.add(Calendar.DAY_OF_MONTH,-razn)

        var dayNow: String
        var monthNow: String
        var yearNow: String

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
        if(dayOfWeek == SUNDAY){
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            return 1
        }
        return dayOfWeek
    }

    private const val SUNDAY = 7


}
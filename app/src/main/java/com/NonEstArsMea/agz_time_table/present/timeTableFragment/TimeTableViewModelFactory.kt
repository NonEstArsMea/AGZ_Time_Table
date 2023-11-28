package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.DateRepository
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetDayOfWeekUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetListOfMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetMonthUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetWeekTimeTableListUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.SetNewCalendarUseCase

class TimeTableViewModelFactory(context: Context) : ViewModelProvider.Factory {


    private val repository = TimeTableRepositoryImpl
    private val dateRepository = DateRepositoryImpl
    private val getWeekTimeTable = GetWeekTimeTableListUseCase(repository, context)
    private val getMonth = GetMonthUseCase(dateRepository, context)
    private val getDayOfWeek = GetDayOfWeekUseCase(dateRepository)
    private val setNewCalendar = SetNewCalendarUseCase(dateRepository)
    private val getListOfMainParam = GetListOfMainParamUseCase(repository)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TimeTableViewModel(
            getWeekTimeTableUseCase = getWeekTimeTable,
            getMonthUseCase = getMonth,
            getDayOfWeekUseCase = getDayOfWeek,
            setNewCalendarUseCase = setNewCalendar,
            getListOfMainParamUseCase = getListOfMainParam
        ) as T
    }
}
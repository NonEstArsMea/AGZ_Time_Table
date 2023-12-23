package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.State.SetTimeTableItemUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetDataUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetDayOfWeekUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetListOfMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetMonthUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetWeekTimeTableListUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.SetNewCalendarUseCase
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimeTableViewModel @Inject constructor(
    private val getWeekTimeTableListUseCase: GetWeekTimeTableListUseCase,
    private val getMonthUseCase: GetMonthUseCase,
    private val getDayOfWeekUseCase: GetDayOfWeekUseCase,
    private val setNewCalendarUseCase: SetNewCalendarUseCase,
    private val getMainParamUseCase: GetMainParamUseCase,
    getDataUseCase: GetDataUseCase,
    private val setTimeTableItem: SetTimeTableItemUseCase,
    private val timeTableRepositoryImpl: TimeTableRepository
) : ViewModel() {


    private var job: Job = viewModelScope.launch { }


    // хранит список с расписанием
    val dataLiveData: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(getDataUseCase.execute()) {
            getNewTimeTable()
        }
    }

    private var _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State>
        get() = _state


    private var lastMainParam: String = ""

    private val _mainParam = getMainParamUseCase.execute()
    val mainParam: LiveData<MainParam>
        get() = _mainParam

    private var currentItem = getDayOfWeekUseCase.execute()

    init {
        getNewTimeTable()
    }


    @SuppressLint("SuspiciousIndentation")
    fun getNewTimeTable(newTime: Int? = null) {
        _state.value = LoadTimeTable
        setNewCalendarUseCase.execute(newTime)
        if (newTime == 0) {
            DateRepositoryImpl.setDayNow()
        }
        currentItem = getCurrenItem(newTime)

        if (job.isActive) {
            job.cancel()
        }

    }

    fun checkMainParam() {
        if (lastMainParam != _mainParam.value?.name) {
            lastMainParam = _mainParam.value?.name.toString()
            getNewTimeTable()
        }
    }


    /**
     * Остановка корутины после завершения работы
     */
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }



    fun startFragment() {
        setTimeTableItem.execute()
    }

    fun getCurrentItem(): Int {
        return currentItem
    }

    fun setCurrentItem(newCurrentItem: Int){
        currentItem = newCurrentItem
    }

    fun getMainCurrentItem(): Int {
        return getDayOfWeekUseCase.execute()
    }

    fun getMonth(): String {
        return getMonthUseCase.execute()
    }

    private fun getCurrenItem(newTime: Int? = null) = (when (newTime) {
        NEXT_WEEK -> {
            FIRST_DAY
        }

        PREVIOUS_WEEK -> {
            LAST_DAY
        }

        else -> {
            null
        }
    }) ?: getDayOfWeekUseCase.execute()

    companion object {
        const val PREVIOUS_WEEK = -7
        const val NEXT_WEEK = 7
        const val NOW_WEEK = 0

        private const val LAST_DAY = 5
        private const val FIRST_DAY = 0
    }


}
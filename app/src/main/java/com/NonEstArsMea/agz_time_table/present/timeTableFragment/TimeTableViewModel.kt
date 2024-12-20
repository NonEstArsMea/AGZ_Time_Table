package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.data.storage.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.util.BottomMenuItemStateManager
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.DataRepository
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.util.DateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimeTableViewModel @Inject constructor(
    private val getMainParamUseCase: GetMainParamUseCase,
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val application: Application,
) : ViewModel() {


    private var job: Job = viewModelScope.launch { }


    private var _state: MutableLiveData<State> = MutableLiveData<State>().apply {
        this.value = LoadData
    }
    val state: LiveData<State>
        get() = _state

    private var _month = MutableLiveData<String>()
    val month: LiveData<String>
        get() = _month

    var mainParam: LiveData<MainParam> = getMainParamUseCase.getLiveData()

    private var lastMainParam: String = EMPTY_STRING

    private var list: List<List<CellClass>> = listOf()


    init {
        _state.value = LoadData
    }

    @SuppressLint("SuspiciousIndentation")
    fun getNewTimeTable(newTime: Int? = null) {
        if (newTime == NOW_WEEK) {
            DateManager.setDayNow()
        } else {
            if (newTime != null) {
                DateManager.setNewCalendar(newTime)
            }
        }
        if (job.isActive) {
            job.cancel()
        }

        _month.value = getMonth()

        job = viewModelScope.launch(Dispatchers.Default) {
            list = timeTableRepositoryImpl.getWeekTimeTable()
            launch(Dispatchers.Main) {
                if(list.size > 0) Log.e("responce", list[0].toString())
                _state.value = TimeTableIsLoad(list)
            }
        }
    }

    fun checkMainParam() {
        val newMainParam = getMainParamUseCase.getNameOfMainParamFromRepo()
            ?: getMainParamUseCase.getNameOfMainParamFromStorage()
        if (lastMainParam != newMainParam) {
            lastMainParam = newMainParam
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
        getNewTimeTable(NOW_WEEK)
        BottomMenuItemStateManager.setNewMenuItem(BottomMenuItemStateManager.TIME_TABLE_ITEM)
    }

    fun getMonth(): String {
        return DateManager.monthAndDayNow(application.applicationContext)
    }

    fun getCurrentItem() = DateManager.getDayOfWeek()

    fun getWeekDateText(): String {
        return DateManager.getWeekDateText(application.applicationContext)
    }

    fun getDateList(): List<String> {
        return DateManager.getDateList()
    }

    companion object {
        const val NOW_WEEK = 0
        const val EMPTY_STRING = ""
    }


}
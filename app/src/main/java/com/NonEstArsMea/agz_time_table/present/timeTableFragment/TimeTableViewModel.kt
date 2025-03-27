package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.NetUtil
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.StorageRepository
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.util.BottomMenuItemStateManager
import com.NonEstArsMea.agz_time_table.util.DateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimeTableViewModel @Inject constructor(
    private val getMainParamUseCase: GetMainParamUseCase,
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val storageRepository: StorageRepository,
    private val application: Application,
    private val net: NetUtil
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

    private val _isConnected: MutableLiveData<Boolean> = net.getNetLiveData()
    val isConnected: LiveData<Boolean>
        get() = _isConnected

    var mainParam: LiveData<MainParam> = getMainParamUseCase.getLiveData()

    private var lastMainParam: String = EMPTY_STRING

    private var list: List<List<CellClass>> = listOf()

    private var dayOfWeek = getCurrentItem()

    private var nowWeek = 0

    private var storageJob: Job = Job()


    init {
        _state.value = LoadData
    }

    fun startLoadTimeTable() {
        storageJob = viewModelScope.launch {
            list = storageRepository.getTimeTableFromStorage()
            _state.value = StorageLoad(list, dayOfWeek)
        }
        loadTimeTable()
    }

    @SuppressLint("SuspiciousIndentation")
    fun getNewTimeTable(newTime: Int? = null) {
        _state.value = LoadData
        if (newTime == 0) {
            nowWeek = newTime
        }
        if (newTime != null) {
            if (newTime == 0) {
                nowWeek = newTime
            } else {
                nowWeek += newTime
            }
            if (nowWeek == NOW_WEEK) {
                dayOfWeek = getCurrentItem()
                DateManager.setDayNow()
            } else {
                dayOfWeek = 0
                DateManager.setNewCalendar(newTime)
            }
        }
        if (job.isActive) {
            job.cancel()
        }

        _month.value = getMonth()

        loadTimeTable()
    }

    fun loadTimeTable() {
        job = viewModelScope.launch(Dispatchers.Default) {
            list = timeTableRepositoryImpl.getWeekTimeTable()
            launch(Dispatchers.Main) {
                storageJob.cancel()
                _state.value = TimeTableIsLoad(list, dayOfWeek)
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
        BottomMenuItemStateManager.setNewMenuItem(BottomMenuItemStateManager.TIME_TABLE_ITEM)
    }

    fun getMonth(): String {
        return DateManager.monthAndDayNow(application.applicationContext)
    }

    private fun getCurrentItem() = DateManager.getDayOfWeek()

    fun getWeekDateText(): String {
        return DateManager.getWeekDateText(application.applicationContext)
    }

    fun getDateList(): List<String> {
        return DateManager.getDateList()
    }

    fun getNextMainParam() {
        timeTableRepositoryImpl.getNextMainParam()
        getNewTimeTable()
    }

    fun ifNotFirstBeginning(): Boolean {
        return timeTableRepositoryImpl.checkFirstBeginning()
    }

    companion object {
        const val NOW_WEEK = 0
        const val EMPTY_STRING = ""
    }


}
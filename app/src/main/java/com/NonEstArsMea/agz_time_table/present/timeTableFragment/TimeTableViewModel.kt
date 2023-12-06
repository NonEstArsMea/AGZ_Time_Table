package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.State.SetTimeTableItemUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetDataUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetDayOfWeekUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetListOfMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetMonthUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetWeekTimeTableListUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.SetNewCalendarUseCase
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimeTableViewModel @Inject constructor(
    private val getWeekTimeTableListUseCase: GetWeekTimeTableListUseCase,
    private val getMonthUseCase: GetMonthUseCase,
    private val getDayOfWeekUseCase: GetDayOfWeekUseCase,
    private val setNewCalendarUseCase: SetNewCalendarUseCase,
    private val getListOfMainParamUseCase: GetListOfMainParamUseCase,
    private val getMainParamUseCase: GetMainParamUseCase,
    getDataUseCase: GetDataUseCase,
    private val setTimeTableItem: SetTimeTableItemUseCase
) : ViewModel() {


    private var job: Job = viewModelScope.launch { }


    private var _timeTableChanged = getWeekTimeTableListUseCase.getArrayOfWeekTimeTable()
    val timeTableChanged: LiveData<List<List<CellApi>>>
        get() = _timeTableChanged

    // хранит список с расписанием
    private var _dataLiveData = getDataUseCase.execute()
    val dataLiveData: LiveData<String>
        get() = _dataLiveData

    // хранит состояние загрузки
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading


    private var dataIsLoad: Boolean = false

    private var dataWasChanged: Boolean = true

    private var lastMainParam: String = ""


    // Хранит параметр поиска
    private val _mainParam = getMainParamUseCase.execute()
    val mainParam: LiveData<MainParam>
        get() = _mainParam


    fun getMainParam(): String {
        return getMainParamUseCase.getNameOfMainParam()
    }

    // создание массивов с расписанием
    fun getTimeTable() {
        if (!dataIsLoad) {
            getNewTimeTable()
            viewModelScope.launch {
                getListOfMainParamUseCase.execute()
            }
            dataIsLoad = true
        }
    }

    fun getNewTimeTable(newTime: Int? = null) {
        setNewCalendarUseCase.execute(newTime)
        if (_dataLiveData.value != null) {
            if (job.isActive) {
                job.cancel()
            }
            dataWasChanged = true
            job = viewModelScope.launch {
                setConditionLoading(true)
                _timeTableChanged.value = getWeekTimeTableListUseCase.execute()
                setConditionLoading(false)
            }
        }
    }

    fun checkMainParam() {
        if (lastMainParam != _mainParam.value?.name) {
            lastMainParam = _mainParam.value?.name!!
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

    /**
    Установка состояние загрузки
     */
    fun setConditionLoading(condition: Boolean) {
        _loading.value = condition
    }


    fun startFragment() {
        setTimeTableItem.execute()
    }

    fun getCurrentItem(): Int {
        return getDayOfWeekUseCase.execute()
    }

    fun getMonth(): String {
        return getMonthUseCase.execute()
    }


}
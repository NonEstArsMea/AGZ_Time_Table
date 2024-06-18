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
    private val repository: DataRepository,
    private val application: Application,
    private val storageRepositoryImpl: StorageRepositoryImpl,
) : ViewModel() {


    private var job: Job = viewModelScope.launch { }


    private var _state: MutableLiveData<State> = MutableLiveData<State>().apply {
        this.value = LoadData
    }
    val state: LiveData<State>
        get() = _state

    var mainParam: LiveData<MainParam> = getMainParamUseCase.getLiveData()

    private var lastMainParam: String = EMPTY_STRING

    private var list: List<List<CellClass>> = listOf()

    private val data = repository.getData()

    private var currentItem: Int? = 0

    init {
        _state.value = LoadData
        data.observeForever {
            if (data.value?.isNotEmpty() == true) {
                getNewTimeTable()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getNewTimeTable(newTime: Int? = null) {
        currentItem = newTime
        _state.postValue(LoadData)
        if (newTime == 0) {
            DateManager.setDayNow()
        } else {
            if (newTime != null) {
                DateManager.setNewCalendar(newTime)
            }
        }
        if (job.isActive) {
            job.cancel()
        }

        viewModelScope.launch(Dispatchers.Default) {
            list = timeTableRepositoryImpl.getWeekTimeTable()
            launch(Dispatchers.Main) {
                Log.e("tag", list.size.toString())
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

    fun timeTableFromStorage(): List<List<CellClass>> {
        Log.e("storrage_4", storageRepositoryImpl.getLastWeekFromStorage().toString())
        return storageRepositoryImpl.getLastWeekFromStorage()
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

    fun getCurrentItem() = (when (currentItem) {
        NEXT_WEEK -> {
            FIRST_DAY
        }

        PREVIOUS_WEEK -> {
            LAST_DAY
        }

        else -> {
            null
        }
    }) ?: DateManager.getDayOfWeek()

    companion object {
        const val PREVIOUS_WEEK = -7
        const val NEXT_WEEK = 7
        const val EMPTY_STRING = ""

        private const val LAST_DAY = 5
        private const val FIRST_DAY = 0
    }


}
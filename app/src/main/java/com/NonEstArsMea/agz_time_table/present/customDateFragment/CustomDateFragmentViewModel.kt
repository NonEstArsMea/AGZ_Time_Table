package com.NonEstArsMea.agz_time_table.present.customDateFragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.util.DateManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomDateFragmentViewModel @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val application: Application,
) : ViewModel() {

    private val uiScope = viewModelScope

    private var job: Job = uiScope.launch { }

    private var _timeTableChanged = MutableLiveData<List<CellClass>>()
    val timeTableChanged: LiveData<List<CellClass>>
        get() = _timeTableChanged


    fun getTimeTable(day: Int, month: Int, year: Int, mainParam: String) {

        val dayOfWeek = "$day-${month + 1}-$year"
        if (job.isActive) {
            job.cancel()
        }
        job = uiScope.launch {
            _timeTableChanged.postValue(
                timeTableRepositoryImpl.preparationData(
                    dayOfWeek,
                    mainParam
                )
            )
        }
    }

    fun getDate(day: Int, month: Int, year: Int): String {
        return "$day ${application.applicationContext.getString(DateManager.getMonth(month))} - $year"
    }


}
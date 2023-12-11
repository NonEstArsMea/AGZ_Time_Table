package com.NonEstArsMea.agz_time_table.present.customDateFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetTimeTableUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetDateUseCase
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomDateFragmentViewModel @Inject constructor(
    private val getTimeTableUseCase: GetTimeTableUseCase,
    private val getDateUseCase: GetDateUseCase
): ViewModel() {

    private val uiScope = viewModelScope

    private var job: Job = uiScope.launch {  }


    private var _timeTableChanged = MutableLiveData<ArrayList<CellApi>>()
    val timeTableChanged: LiveData<ArrayList<CellApi>>
        get() = _timeTableChanged


    fun getTimeTable(day: Int, month: Int, year: Int, mainParam: String){

            val dayOfWeek = "$day-${month+1}-$year"
            if (job.isActive) {
                job.cancel()
            }
            job = uiScope.launch {
                _timeTableChanged.postValue(getTimeTableUseCase.execute(dayOfWeek, mainParam))
            }
    }

    fun getDate(day: Int, month: Int, year: Int): String{
        return getDateUseCase.execute(day, month, year)
    }


}
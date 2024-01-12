package com.NonEstArsMea.agz_time_table.present.examsFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExamsFragmentViewModel @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository,
) : ViewModel() {

    private var job: Job = viewModelScope.launch { }

    private var mainParam: String? = null

    private var _timeTableChanged = MutableLiveData<List<CellClass>>()
    val timeTableChanged: LiveData<List<CellClass>>
        get() = _timeTableChanged


    private fun getTimeTable() {

        if (job.isActive) {
            job.cancel()
        }
        if (mainParam != null) {
            job = viewModelScope.launch {
                Log.e("exams", mainParam.toString())
                _timeTableChanged.postValue(
                    timeTableRepositoryImpl.getExams(mainParam!!)
                )
            }

        }

    }

    fun setMainParam(newMainParam: String) {
        mainParam = newMainParam
        getTimeTable()
    }

}
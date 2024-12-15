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


    private var _timeTableChanged = MutableLiveData<List<CellClass>>()
    val timeTableChanged: LiveData<List<CellClass>>
        get() = _timeTableChanged


    fun getTimeTable() {

        if (job.isActive) {
            job.cancel()
        }
        job = viewModelScope.launch {

            _timeTableChanged.postValue(
                timeTableRepositoryImpl.getExams()
            )

        }


    }


}
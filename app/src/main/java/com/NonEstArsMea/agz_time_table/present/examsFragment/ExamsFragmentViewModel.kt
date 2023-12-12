package com.NonEstArsMea.agz_time_table.present.examsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetExamsUseCase
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExamsFragmentViewModel @Inject constructor(
    private val getExams: GetExamsUseCase
): ViewModel() {

    private var job: Job = viewModelScope.launch {  }


    private var _timeTableChanged = MutableLiveData<ArrayList<CellApi>>()
    val timeTableChanged: LiveData<ArrayList<CellApi>>
        get() = _timeTableChanged

    fun getTimeTable(mainParam: String){

        if (job.isActive) {
            job.cancel()
        }
        job = viewModelScope.launch {
            _timeTableChanged.postValue(
                getExams.execute(mainParam)
            )
        }
    }

}
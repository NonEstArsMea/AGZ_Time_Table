package com.NonEstArsMea.agz_time_table.present.examsFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ExamsFragmentViewModel: ViewModel() {

    private var job: Job = viewModelScope.launch {  }


    private var _timeTableChanged = MutableLiveData<ArrayList<CellApi>>()
    val timeTableChanged: LiveData<ArrayList<CellApi>>
        get() = _timeTableChanged

    fun getTimeTable(mainParam: String){

        if (job.isActive) {
            job.cancel()
        }
        job = viewModelScope.launch {
            _timeTableChanged.postValue(TimeTableRepositoryImpl.getExams(
                DataRepositoryImpl.getContent(), mainParam))
        }
    }

}
package com.NonEstArsMea.agz_time_table.present.customDateFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CastomDateFragmentViewModel: ViewModel() {

    private var jobVM = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + jobVM)

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
                //_timeTableChanged.postValue()
            }
    }


}
package com.NonEstArsMea.agz_time_table.present.examsFragment

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

class ExamsFragmentViewModel: ViewModel() {
    private var jobVM = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + jobVM)

    private var job: Job = uiScope.launch {  }


    // хранит состояние загрузки
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _timeTableChanged = MutableLiveData<ArrayList<CellApi>>()
    val timeTableChanged: LiveData<ArrayList<CellApi>>
        get() = _timeTableChanged
    fun getTimeTable(mainParam: String){

        if (job.isActive) {
            job.cancel()
        }
        job = uiScope.launch {
            try {
                setConditionLoading(true)
                Log.e("CDFVM", mainParam.toString())
                _timeTableChanged.value = TimeTableRepositoryImpl.getExams(
                    DataRepositoryImpl.getContent(), mainParam)
                setConditionLoading(false)
            } catch (e: Exception) {
                Log.e("CDFVM", e.toString())
            }
        }
    }
    fun setConditionLoading(condition: Boolean){
        _loading.value = condition
    }
}
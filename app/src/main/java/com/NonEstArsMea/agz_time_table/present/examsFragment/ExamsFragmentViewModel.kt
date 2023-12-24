package com.NonEstArsMea.agz_time_table.present.examsFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetDataUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetExamsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExamsFragmentViewModel @Inject constructor(
    private val getExams: GetExamsUseCase,
    getDataUseCase: GetDataUseCase,
) : ViewModel() {

    private var job: Job = viewModelScope.launch { }

    private var mainParam: String? = null

    private var _timeTableChanged = MutableLiveData<List<CellApi>>()
    val timeTableChanged: LiveData<List<CellApi>>
        get() = _timeTableChanged

//    val dataLiveData = MediatorLiveData<String>().apply {
//        addSource(getDataUseCase.execute()) {
//            getTimeTable()
//        }
//    }



    private fun getTimeTable() {

        if (job.isActive) {
            job.cancel()
        }
        if (mainParam != null) {
            job = viewModelScope.launch {
                Log.e("exams", mainParam.toString())
                _timeTableChanged.postValue(
                    getExams.execute(mainParam!!)
                )
            }

        }

    }

    fun setMainParam(newMainParam: String) {
        mainParam = newMainParam
        getTimeTable()
    }

}
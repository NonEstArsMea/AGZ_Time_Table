package com.NonEstArsMea.agz_time_table.present.cafTimeTable

import android.util.Log
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

class CafTimeTableViewModel@Inject constructor(
    private val repository: TimeTableRepository
) : ViewModel() {

    private var _state = MutableLiveData<CafTimeTableState>()
    val state: LiveData<CafTimeTableState>
        get() = _state

    private var offset = 0
    private var date = DateManager.getFullDateNow(offset)
    private var id = ""
    private var cafName = ""
    private var rep: Map<String, List<CellClass>> = mapOf()
    private lateinit var unicList: List<String>

    private var currentJob: Job? = null

    init {
        _state.value = SetDate(date, id)
    }

    fun setNewDate(day: Int) {
        offset += day
        date = DateManager.getFullDateNow(offset)
        _state.value = SetDate(date, id)
        getData(cafName, id)
    }

    fun getData(name:String, cafID: String) {
        id = cafID
        cafName = name
        Log.e("log", id.toString())
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            rep = repository.getCafTimeTable(date, id)
            Log.e("rep", "start " + date.toString() + rep.toString())
            setData()
        }
    }

    private fun setData() {
        if (rep.isNotEmpty()) {
            Log.e("rep", "start " + rep.toString())
            unicList = getAllKeys()
            Log.e("rep", unicList.toString())
            _state.value = DataIsLoad(date, unicList, rep, cafName)
        }else{
            _state.value = SetDate(date, cafName)
        }


    }

    private fun getAllKeys(): List<String> {
        return rep.keys.toList()
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }

}
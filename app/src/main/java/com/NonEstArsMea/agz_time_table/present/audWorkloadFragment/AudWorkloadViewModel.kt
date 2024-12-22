package com.NonEstArsMea.agz_time_table.present.audWorkloadFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.util.DateManager
import dagger.Provides
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudWorkloadViewModel @Inject constructor(
    private val repository: TimeTableRepository
) : ViewModel() {

    private var _position = MutableLiveData(0)
    val position: LiveData<Int>
        get() = _position

    private var _state = MutableLiveData<WorkloadState>()
    val state: LiveData<WorkloadState>
        get() = _state

    private var offset = 0
    private var date = DateManager.getFullDateNow(offset)
    private var rep: Map<String, List<CellClass>> = mapOf()
    private lateinit var unicList: List<String>
    private var numberOfBuilding: String = "1"

    private var currentJob: Job? = null

    init {
        _state.value = SetDate(date)
    }

    fun setPosition(p: Int) {
        _position.value = p
    }

    fun setNewDate(day: Int) {
        offset += day
        date = DateManager.getFullDateNow(offset)
        getData()
    }

    fun getData() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            rep = repository.getListOfAudWorkload(date)
            getNewBuilding(0)
        }
    }

    fun getNewBuilding(position: Int) {
        numberOfBuilding = when (position) {
            0 -> "1"
            1 -> "2"
            2 -> "3"
            3 -> "4"
            else -> {
                "1"
            }
        }
        if (rep.isNotEmpty()) {
            rep[numberOfBuilding]?.let {
                val uniqueCells = it.distinctBy { cell ->
                    cell.classroom to cell.subjectNumber
                }
                val sortedList = uniqueCells.sortedBy { cell ->
                    cell.classroom!!.trim().split("/").getOrNull(1)?.toIntOrNull() ?: 0
                }
                unicList = getUniqueСlass()
                _state.value = DataIsLoad(date, unicList, sortedList)
            }
        }


    }

    private fun getUniqueСlass(): List<String> {
        val list = mutableListOf<String>()
        rep[numberOfBuilding]?.forEach {
            if (it.classroom !in list) list.add(it.classroom!!)
        }
        val sortedList = list.sortedBy {
            it.trim().split("/").getOrNull(1)?.toIntOrNull() ?: 0
        }
        return sortedList
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }


    companion object {

    }

}
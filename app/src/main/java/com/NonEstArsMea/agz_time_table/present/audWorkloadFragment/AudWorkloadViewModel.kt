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


    private var _state = MutableLiveData<WorkloadState>()
    val state: LiveData<WorkloadState>
        get() = _state

    private var offset = 0
    private var date = DateManager.getFullDateNow(offset)
    private var rep: List<List<CellClass>> = listOf()
    private lateinit var unicList: List<String>
    private var numberOfBuilding: Int = 0

    private var currentJob: Job? = null

    init {
        _state.value = SetDate(date)
        getData()
    }

    fun setPosition(p: Int) {
        getNewBuilding(p)
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

    private fun getNewBuilding(position: Int) {
        numberOfBuilding = position
        if (rep.isNotEmpty()) {
            Log.e("rep", "start " + rep.toString())
            rep[numberOfBuilding].let {
                val uniqueCells = it.distinctBy { cell ->
                    cell.classroom to cell.subjectNumber
                }
                val sortedList = uniqueCells.sortedBy { cell ->
                    cell.classroom.trim().split("/").getOrNull(1)?.toIntOrNull() ?: 0
                }
                unicList = getUniqueСlass()
                Log.e("rep", unicList.toString())
                _state.value = DataIsLoad(date, unicList, sortedList, position)
            }
        }


    }

    private fun getUniqueСlass(): List<String> {
        val list = mutableListOf<String>()
        rep[numberOfBuilding].forEach {
            if (it.classroom !in list) list.add(it.classroom)
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
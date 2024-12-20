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

    private val date = DateManager.getFullDateNow()

    private var rep: Map<String, List<CellClass>> = mapOf()
    private lateinit var unicList: List<String>
    private var numberOfBuilding: String = "1"

    init {
        _state.value = SetDate(date)
    }

    fun setPosition(p: Int) {
        _position.value = p
    }

    fun getData() {
        viewModelScope.launch {
            rep = repository.getListOfAudWorkload(date)
            unicList = getUniqueСlass()
            rep[numberOfBuilding]?.let {
                _state.value = DataIsLoad(date, unicList, it)
            }
        }
    }

    fun getNewBuilding(position: Int) {
        numberOfBuilding = when(position){
            0 -> "1"
            1 -> "2"
            2 -> "3"
            3 -> "4"
            else -> {"1"}
        }
        if(rep.isNotEmpty()){
            rep[numberOfBuilding]?.let {
                unicList = getUniqueСlass()
                _state.value = DataIsLoad(date, unicList, it)
            }
        }


    }

    private fun getUniqueСlass(): List<String> {
        val list = mutableListOf<String>()
        rep[numberOfBuilding]?.forEach {
            if (it.classroom !in list) list.add(it.classroom!!)
        }
        return list
    }

    companion object {

    }

}
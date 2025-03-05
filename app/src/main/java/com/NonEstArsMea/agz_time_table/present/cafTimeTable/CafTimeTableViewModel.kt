package com.NonEstArsMea.agz_time_table.present.cafTimeTable

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.StorageRepository
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository

import com.NonEstArsMea.agz_time_table.util.DateManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class CafTimeTableViewModel@Inject constructor(
    private val repository: TimeTableRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private var _state = MutableLiveData<CafTimeTableState>()
    val state: LiveData<CafTimeTableState>
        get() = _state

    private var offset = 0
    private var date = DateManager.getFullDateNow(offset)
    private var id = INDEFINITE_ID
    private var cafName = INDEFINITE_NAME
    private var rep: Map<String, List<CellClass>> = mapOf()
    private lateinit var unicList: List<String>

    private var currentJob: Job? = null

    init {
        id = storageRepository.getCafIdInStorage()
        _state.value = SetDate(date, id)
        val index = ItemsAdapter.items.indexOf(id)
        if(index != INDEFINITE_INDEX){
            cafName = ItemsAdapter.names[index]
        }
        getData(cafName, id)
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
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            rep = repository.getCafTimeTable(date, id)
            setData()
        }
    }

    private fun setData() {
        if (rep.isNotEmpty()) {
            unicList = getAllKeys()
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

    fun setNewID(id: String) {
        storageRepository.setCafIdInStorage(id)
    }

    companion object{
        private const val INDEFINITE_ID = ""
        private const val INDEFINITE_NAME = ""
        private const val INDEFINITE_INDEX = -1
        const val TEACHER_LIST_KEY = 1
        const val GROUP_LIST_KEY = 2
        const val BUNDLE_KEY = "BK"
    }

}
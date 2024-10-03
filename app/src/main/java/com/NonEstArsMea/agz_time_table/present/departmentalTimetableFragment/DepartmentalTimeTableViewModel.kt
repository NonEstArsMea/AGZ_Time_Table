package com.NonEstArsMea.agz_time_table.present.departmentalTimetableFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.LoadData
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.State
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.TimeTableIsLoad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DepartmentalTimeTableViewModel @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository
) : ViewModel() {


    private var _state: MutableLiveData<State> = MutableLiveData<State>().apply {
        this.value = LoadData
    }
    val state: LiveData<State>
        get() = _state

    private var list: List<List<CellClass>> = listOf()

    fun getTimeTable(
        departmentId: String,
        date: String
    ) {
        _state.postValue(LoadData)
        viewModelScope.launch(Dispatchers.Default) {
            list = timeTableRepositoryImpl.getDepartmentTimeTable(departmentId, date)
            launch(Dispatchers.Main) {
                _state.value = TimeTableIsLoad(list)
            }
        }
    }

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    fun setText(newText: String) {
        _text.value = newText
    }

    fun getDepartment(): List<String>  = timeTableRepositoryImpl.getDepartment()

}
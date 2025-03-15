package com.NonEstArsMea.agz_time_table.present.workloadDetailInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class WorkloadDetailInfoViewModel @Inject constructor(
    val repository: TimeTableRepository
) : ViewModel() {

    private var _state = MutableLiveData<DetailState>()

    val state: LiveData<DetailState>
        get() = _state

    fun getData(month: String, department: String) {
        viewModelScope.launch {
            val list = repository.getListOfDetailedWorkload(month, department)
            _state.postValue(DataIsLoad(list, department, month))
        }
    }
}
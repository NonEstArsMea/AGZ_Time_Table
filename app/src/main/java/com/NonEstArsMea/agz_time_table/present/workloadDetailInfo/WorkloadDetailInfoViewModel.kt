package com.NonEstArsMea.agz_time_table.present.workloadDetailInfo

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.NonEstArsMea.agz_time_table.util.Methods
import kotlinx.coroutines.launch
import javax.inject.Inject

class WorkloadDetailInfoViewModel @Inject constructor(
    val repository: TimeTableRepository,
    private val resources: Resources,
) : ViewModel() {

    private var _state = MutableLiveData<DetailState>()

    val state: LiveData<DetailState>
        get() = _state


    fun getData(month: String, department: String, mainParam: String) {
        viewModelScope.launch {
            val year = DateManager.getYear()
            val map = repository.getListOfDetailedWorkload(month, department, year, mainParam)

            val list = mapToList(map, "$month-$year")
            _state.postValue(DataIsLoad(list, department, month))
        }
    }

    private fun mapToList(map: Map<String, List<CellClass>>, date: String): List<CellClass> {
        var newDate = ""
        val list = mutableListOf<CellClass>()
        if(map.isNotEmpty()){
            (1..31).forEach {
                newDate = "$it-$date"
                if(!(map[newDate].isNullOrEmpty())){
                    list.addAll(map[newDate]!!)
                }
            }
        }

        list.forEach { lesson ->
            lesson.color = Methods.setColor(lesson.subjectType)
            lesson.subjectType =
                resources.getString(Methods.returnFullNameOfTheItemType(lesson.subjectType))
        }


        return list
    }
}
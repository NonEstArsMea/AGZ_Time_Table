package com.NonEstArsMea.agz_time_table.present.workloadLayout

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.storage.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.StorageRepository
import com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView.CafClass
import com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView.MonthName
import com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView.RWWorkloadClass
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.NonEstArsMea.agz_time_table.util.Methods
import com.NonEstArsMea.agz_time_table.util.getFullName
import kotlinx.coroutines.launch
import javax.inject.Inject

class WorkloadViewModel @Inject constructor(
    private val repository: TimeTableRepositoryImpl,
    private val storageRepository: StorageRepository,
    @SuppressLint("StaticFieldLeak") private val context: Context
) : ViewModel() {

    private var _state = MutableLiveData<WorkloadState>()
    val state: LiveData<WorkloadState>
        get() = _state

    private var name = "Выберите перподавателя"

    init {
        name = storageRepository.getNameTeacherWorkload()
        if(name != StorageRepositoryImpl.ERROR_VALUE) getData(name)
    }

    fun getData(name: String) {
        viewModelScope.launch {
            val teacherWorkload = repository.getTeacherWorkload(name)
            val prepData = prepareData(teacherWorkload)
            _state.postValue(DataIsLoad(prepData, name))
        }
    }

    fun sedNameInStorage(name:String){
        storageRepository.setNameTeacherWorkloadInStorage(name)
    }

    fun prepareData(data: Map<String, Map<String, Map<String, Int>>>): List<RWWorkloadClass> {
        val list = mutableListOf<RWWorkloadClass>()
        if (data.isNotEmpty()) {
            for ((month, departmentMap) in data) {

                list.add(MonthName(month))
                for ((department, hoursMap) in departmentMap) {
                    var hoursList = ""
                    for ((hours, value) in hoursMap) {
                        hoursList = hoursList +
                            context.getString(Methods.returnFullNameOfTheItemType(hours)) + " - " + (value * 2).toString() + " часов(a)\n"
                    }
                    list.add(CafClass( hoursList, department, month))
                }
            }
        }
        return list
    }
}
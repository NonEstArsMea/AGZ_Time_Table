package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

class GetMainParamUseCase(private val repository: TimeTableRepository) {
    fun execute():MutableLiveData<MainParam>{
        return repository.getMainParam()
    }

    fun getNameOfMainParam():String{
        return repository.getMainParam().value!!.name
    }
}
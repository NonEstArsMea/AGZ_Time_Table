package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class GetMainParamUseCase @Inject constructor(private val repository: TimeTableRepository) {
    fun execute():MutableLiveData<MainParam>{
        return repository.getMainParam()
    }

    fun getNameOfMainParam():String{
        return repository.getMainParam().value!!.name
    }
}
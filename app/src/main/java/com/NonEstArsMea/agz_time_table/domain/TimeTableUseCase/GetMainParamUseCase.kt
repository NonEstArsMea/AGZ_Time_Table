package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class GetMainParamUseCase @Inject constructor() {
    fun execute():MutableLiveData<MainParam>{
        return TimeTableRepositoryImpl.getMainParam()
    }

    fun getNameOfMainParam():String{
        return TimeTableRepositoryImpl.getMainParam().value?.name ?: throw RuntimeException("Unknown mainParam")
    }
}
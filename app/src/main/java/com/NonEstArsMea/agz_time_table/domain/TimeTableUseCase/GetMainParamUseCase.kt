package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import androidx.lifecycle.LiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class GetMainParamUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository
) {
    fun execute(): LiveData<MainParam> {
        return timeTableRepositoryImpl.getMainParam()
    }

    fun getNameOfMainParam():String{
        return timeTableRepositoryImpl.getMainParam().value?.name ?: throw RuntimeException("Unknown mainParam")
    }
}
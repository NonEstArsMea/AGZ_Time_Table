package com.NonEstArsMea.agz_time_table.domain.settingUseCase

import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class SetMainParamUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository
){

    fun execute(mainParam: MainParam){
        timeTableRepositoryImpl.setMainParam(mainParam)
    }
}
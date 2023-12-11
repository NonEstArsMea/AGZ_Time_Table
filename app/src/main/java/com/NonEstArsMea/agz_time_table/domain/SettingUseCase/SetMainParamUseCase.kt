package com.NonEstArsMea.agz_time_table.domain.SettingUseCase

import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class SetMainParamUseCase @Inject constructor(){

    fun execute(mainParam: MainParam){
        TimeTableRepositoryImpl.setMainParam(mainParam)
    }
}
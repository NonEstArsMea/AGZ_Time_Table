package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

class GetListOfMainParamUseCase(private val timeTableRepository: TimeTableRepository) {
    fun execute(newData: String):List<MainParam>{
        return timeTableRepository.getListOfMainParam(newData)
    }
}
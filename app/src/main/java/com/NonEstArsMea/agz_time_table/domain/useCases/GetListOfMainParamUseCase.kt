package com.NonEstArsMea.agz_time_table.domain.useCases

import com.NonEstArsMea.agz_time_table.domain.TimeTableRepository
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

class GetListOfMainParamUseCase(private val timeTableRepository: TimeTableRepository) {
    fun execute(newData: String, arrOfMainParams: ArrayList<MainParam>?):List<MainParam>{
        return timeTableRepository.getListOfMainParam(newData, arrOfMainParams)
    }
}
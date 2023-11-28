package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

class GetListOfMainParamUseCase(private val timeTableRepository: TimeTableRepository) {
    fun execute(){
        return timeTableRepository.getListOfMainParam()
    }
}
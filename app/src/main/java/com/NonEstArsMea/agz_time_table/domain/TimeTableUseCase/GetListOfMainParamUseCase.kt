package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import javax.inject.Inject

class GetListOfMainParamUseCase @Inject constructor(private val timeTableRepository: TimeTableRepository) {
    fun execute(){
        return timeTableRepository.getListOfMainParam()
    }
}
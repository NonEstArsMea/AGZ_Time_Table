package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import javax.inject.Inject

class GetListOfMainParamUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository
) {
    fun execute() {
        return timeTableRepositoryImpl.getListOfMainParam()
    }
}
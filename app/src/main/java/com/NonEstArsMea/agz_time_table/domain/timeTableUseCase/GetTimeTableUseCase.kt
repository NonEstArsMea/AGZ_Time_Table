package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import javax.inject.Inject

class GetTimeTableUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository
) {

    suspend fun execute(dayOfWeek: String, mainParam: String): List<CellApi> {
        return timeTableRepositoryImpl.preparationData(dayOfWeek, mainParam)
    }
}
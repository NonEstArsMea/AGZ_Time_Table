package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import javax.inject.Inject

class GetExamsUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository
) {

    suspend fun execute(mainParam: String): List<CellApi> {
        return timeTableRepositoryImpl.getExams(mainParam)
    }
}
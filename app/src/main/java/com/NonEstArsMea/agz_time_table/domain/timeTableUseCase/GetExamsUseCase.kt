package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import com.NonEstArsMea.agz_time_table.data.net.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import javax.inject.Inject

class GetExamsUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val dataRepositoryImpl: DataRepositoryImpl
) {

    suspend fun execute(mainParam: String): List<CellApi> {
        return if(dataRepositoryImpl.getContent().isNotEmpty())
            timeTableRepositoryImpl.getExams(mainParam)
        else
            listOf()
    }
}
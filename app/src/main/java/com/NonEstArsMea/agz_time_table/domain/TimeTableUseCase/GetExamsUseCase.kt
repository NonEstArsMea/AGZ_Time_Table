package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class GetExamsUseCase @Inject constructor(private val repository: TimeTableRepository) {

    suspend fun execute(mainParam: String): ArrayList<CellApi> {
        return repository.getExams(mainParam)
    }
}
package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class GetExamsUseCase @Inject constructor(private val repository: TimeTableRepository, private val context: Context) {

    suspend fun execute(mainParam: String): ArrayList<CellApi> {
        return repository.getExams(mainParam, context)
    }
}
package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import javax.inject.Inject

class GetExamsUseCase @Inject constructor(private val context: Context) {

    suspend fun execute(mainParam: String): ArrayList<CellApi> {
        return TimeTableRepositoryImpl.getExams(mainParam, context)
    }
}
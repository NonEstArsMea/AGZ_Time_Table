package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import javax.inject.Inject

class GetTimeTableUseCase @Inject constructor(private val context: Context) {

    suspend fun execute(dayOfWeek: String, mainParam: String): ArrayList<CellApi> {
        return TimeTableRepositoryImpl.preparationData(dayOfWeek, mainParam, context)
    }
}
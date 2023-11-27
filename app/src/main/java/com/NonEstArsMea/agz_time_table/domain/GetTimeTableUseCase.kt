package com.NonEstArsMea.agz_time_table.domain

import android.content.Context
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

class GetTimeTableUseCase(private val context: Context) {

    suspend fun execute(dayOfWeek: String, mainParam: String): ArrayList<CellApi> {
        return TimeTableRepositoryImpl.preparationData("", dayOfWeek, mainParam, context)
    }
}
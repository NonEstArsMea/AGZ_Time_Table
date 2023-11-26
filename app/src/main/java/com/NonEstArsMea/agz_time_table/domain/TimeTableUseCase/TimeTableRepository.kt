package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

interface TimeTableRepository {


    suspend fun getWeekTimeTable(newData: String, context: Context): List<List<CellApi>>


    suspend fun preparationData(data: String,
                                dayOfWeek: String,
                                mainParam: String,
                                context: Context): List<CellApi>
    fun getListOfMainParam(data: String)
}
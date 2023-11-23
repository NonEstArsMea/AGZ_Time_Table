package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

interface TimeTableRepository {


    suspend fun getWeekTimeTable(newData: String): List<List<CellApi>>


    suspend fun preparationData(data: String,
                                dayOfWeek: String = "10-02-2023",
                                mainParam: String = "314"): List<CellApi>
    fun getListOfMainParam(data: String)
}
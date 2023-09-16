package com.NonEstArsMea.agz_time_table.domain

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

interface TimeTableRepository {

    fun getDayTimeTable(dayOfWeek: Int): ArrayList<CellApi>

    suspend fun getWeekTimeTable(newData: String,
                         dayOfWeek: ArrayList<String>,
                         mainParam: String): ArrayList<ArrayList<CellApi>>

    fun getLessonTimeTable(numberLesson: Int,
                           dayOfWeek: Int): CellApi

    suspend fun preparationData(data: String,
                                dayOfWeek: String = "10-02-2023",
                                mainParam: String = "314"): ArrayList<CellApi>
    fun getListOfMainParam(data: String, arrOfMainParams: ArrayList<MainParam>?): List<MainParam>
}
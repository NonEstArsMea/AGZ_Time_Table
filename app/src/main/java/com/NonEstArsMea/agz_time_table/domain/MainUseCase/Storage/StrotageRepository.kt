package com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.LessonTimeTable
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

interface StrotageRepository {

    fun getMainParamFromStorage() :String

    fun getFavoriteMainParamsFromStorage():ArrayList<MainParam>

    fun getLastWeekFromStorage():ArrayList<ArrayList<CellApi>>
    fun setDataInStorage(mainParam: String?,
                         favMainParamList: ArrayList<MainParam>?,
                         lastWeekTimeTable: ArrayList<ArrayList<CellApi>>?)
}
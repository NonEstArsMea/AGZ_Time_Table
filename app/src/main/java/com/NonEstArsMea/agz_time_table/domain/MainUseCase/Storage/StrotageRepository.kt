package com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

interface StrotageRepository {

    fun getMainParamFromStorage() :MainParam

    fun getFavoriteMainParamsFromStorage():ArrayList<MainParam>

    fun getLastWeekFromStorage():ArrayList<ArrayList<CellApi>>

    fun getThemeFromStorage():Int
    fun setDataInStorage(mainParam: MainParam?,
                         favMainParamList: ArrayList<MainParam>?,
                         lastWeekTimeTable: List<List<CellApi>>?,
                         theme: Int?)
}
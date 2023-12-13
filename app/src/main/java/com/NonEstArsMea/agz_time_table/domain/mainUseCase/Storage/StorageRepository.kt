package com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

interface StorageRepository {

    fun getMainParamFromStorage() :MainParam

    fun getFavoriteMainParamsFromStorage():ArrayList<MainParam>

    fun getLastWeekFromStorage():ArrayList<ArrayList<CellApi>>

    fun getThemeFromStorage():Int
    fun setDataInStorage(mainParam: MainParam?,
                         favMainParamList: ArrayList<MainParam>?,
                         lastWeekTimeTable: List<List<CellApi>>?,
                         theme: Int?)
}
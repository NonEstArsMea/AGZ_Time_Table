package com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

interface StorageRepository {

    fun getMainParamFromStorage(): MainParam

    fun getFavoriteMainParamsFromStorage(): ArrayList<MainParam>

    fun getNameTeacherWorkload(): String


    fun getThemeFromStorage(): Int
    fun setDataInStorage(
        mainParam: MainParam?,
        favMainParamList: ArrayList<MainParam>?,
        theme: Int?,
        list: List<List<CellClass>>
    )

    fun setMainParamInStorage(mainParam: MainParam?)

    fun setCafIdInStorage(id: String)

    fun getCafIdInStorage(): String

    fun getTimeTableFromStorage(): List<List<CellClass>>
    fun setNameTeacherWorkloadInStorage(name: String)
}
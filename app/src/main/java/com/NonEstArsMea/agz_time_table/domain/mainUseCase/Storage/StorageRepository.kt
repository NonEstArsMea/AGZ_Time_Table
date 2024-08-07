package com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage

import androidx.lifecycle.LiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

interface StorageRepository {

    fun getMainParamFromStorage(): MainParam

    fun getFavoriteMainParamsFromStorage(): ArrayList<MainParam>

    fun getLastWeekFromStorage(): List<List<CellClass>>

    fun setTimeTableInStorage(list: List<List<CellClass>>?)

    fun getThemeFromStorage(): Int
    fun setDataInStorage(
        mainParam: MainParam?,
        favMainParamList: ArrayList<MainParam>?,
        theme: Int?,
    )
}
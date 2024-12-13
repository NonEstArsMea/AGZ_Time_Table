package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam


interface TimeTableRepository {


    suspend fun getWeekTimeTable(): List<List<CellClass>>

    suspend fun getListOfMainParam()
    fun getNewListOfMainParam(): MutableLiveData<ArrayList<MainParam>>

    fun getMainParam(): MutableLiveData<MainParam>
    fun getStringMainParam(): String
    fun setMainParam(newMainParam: MainParam)

    fun setWeekTimeTable(list: LiveData<List<List<CellClass>>>)
    fun getArrayOfWeekTimeTable(): MutableLiveData<List<List<CellClass>>>

    fun getArrayOfFavoriteMainParam(): MutableLiveData<ArrayList<MainParam>>
    fun setListOfFavoriteMainParam(list: ArrayList<MainParam>)
    fun updateFavoriteParamList(newMainParam: MainParam)

    suspend fun getExams(mainParam: String): ArrayList<CellClass>

    suspend fun getDepartmentTimeTable(
        departmentId: String,
        date: String
    ): List<List<CellClass>>

    fun getDepartment(): List<String>
}

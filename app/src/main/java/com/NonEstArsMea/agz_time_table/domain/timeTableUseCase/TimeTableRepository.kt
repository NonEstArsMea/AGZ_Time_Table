package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam


interface TimeTableRepository {


    suspend fun getWeekTimeTable(): List<List<CellClass>>

    suspend fun getListOfGroups()

    suspend fun getListOfTeachers()
    suspend fun getListOfAudWorkload(date: String): List<List<CellClass>>

    suspend fun getCafTimeTable(date: String, id: String): Map<String, List<CellClass>>

    fun getNewListOfMainParam(): MutableLiveData<ArrayList<MainParam>>

    fun getMainParam(): MutableLiveData<MainParam>
    fun getStringMainParam(): String
    fun setMainParam(newMainParam: MainParam)

    fun setWeekTimeTable(list: LiveData<List<List<CellClass>>>)
    fun getArrayOfWeekTimeTable(): MutableLiveData<List<List<CellClass>>>

    fun getArrayOfFavoriteMainParam(): MutableLiveData<ArrayList<MainParam>>
    fun setListOfFavoriteMainParam(list: ArrayList<MainParam>)
    fun updateFavoriteParamList(newMainParam: MainParam)

    suspend fun getExams(): List<CellClass>

    fun getDepartment(): List<String>
    fun getNextMainParam(): String
    fun checkFirstBeginning(): Boolean

    fun moveItemInFavoriteMainParam(param: MainParam): ArrayList<MainParam>
    suspend fun getTeacherWorkload(name: String): Map<String, Map<String, Map<String, Int>>>
    suspend fun getListOfDetailedWorkload(
        month: String, department: String, year: String,
        mainParam: String
    ): Map<String, List<CellClass>>

    fun clearListOfMainParam()
}

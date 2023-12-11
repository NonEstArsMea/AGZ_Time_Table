package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam


interface TimeTableRepository {


    suspend fun getWeekTimeTable(context: Context): List<List<CellApi>>


    suspend fun preparationData(dayOfWeek: String,
                                mainParam: String,
                                context: Context): List<CellApi>
    fun getListOfMainParam(context: Context)
    fun getNewListOfMainParam(): MutableLiveData<ArrayList<MainParam>>

    fun getMainParam(): MutableLiveData<MainParam>
    fun setMainParam(newMainParam: MainParam)

    fun setWeekTimeTable(list: ArrayList<ArrayList<CellApi>>)
    fun getArrayOfWeekTimeTable(): MutableLiveData<List<List<CellApi>>>

    fun getArrayOfFavoriteMainParam(): MutableLiveData<ArrayList<MainParam>>
    fun setListOfFavoriteMainParam(list: ArrayList<MainParam>)
    fun updateFavoriteParamList(newMainParam: MainParam)

    fun getTheme(): MutableLiveData<Int>
    fun setTheme(newTheme: Int)

    suspend fun getExams(mainParam: String, context: Context): ArrayList<CellApi>


}
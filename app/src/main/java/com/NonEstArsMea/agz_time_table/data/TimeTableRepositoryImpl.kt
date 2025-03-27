package com.NonEstArsMea.agz_time_table.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.net.retrofit.Common
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.NetUtil
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.StorageRepository
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.NonEstArsMea.agz_time_table.util.Methods
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeTableRepositoryImpl @Inject constructor(
    private val resources: Resources,
    private val repository: StorageRepository,
    private val netUtil: NetUtil,
) : TimeTableRepository {

    private var weekTimeTable = MutableLiveData<List<List<CellClass>>>()
    private var mainParam = MutableLiveData<MainParam>()
    private var listOfMainParam = MutableLiveData<ArrayList<MainParam>>()
    private var listOfFavoriteMainParam = MutableLiveData<ArrayList<MainParam>>()


    override fun getMainParam(): MutableLiveData<MainParam> {
        return mainParam
    }

    override fun getStringMainParam(): String {
        return if (mainParam.value != null) {
            mainParam.value!!.name
        } else {
            resources.getString(R.string.name_param_is_null)
        }
    }

    override fun getArrayOfFavoriteMainParam(): MutableLiveData<ArrayList<MainParam>> {
        return listOfFavoriteMainParam
    }

    override fun getArrayOfWeekTimeTable(): MutableLiveData<List<List<CellClass>>> {
        return weekTimeTable
    }

    override fun setMainParam(newMainParam: MainParam) {
        if (mainParam.value != newMainParam) {
            mainParam.value = newMainParam
        }
    }

    override fun setListOfFavoriteMainParam(list: ArrayList<MainParam>) {
        listOfFavoriteMainParam.value = list
    }

    override fun setWeekTimeTable(list: LiveData<List<List<CellClass>>>) {
        weekTimeTable = list as MutableLiveData<List<List<CellClass>>>
    }


    override suspend fun getExams(): List<CellClass> {
        try {
            val response = Common.retrofitService.getExams(mainParam.value!!.name)
            if (response.isSuccessful) {
                return if (!response.body().isNullOrEmpty()) {
                    val exams = response.body()!!
                    exams.forEach { lesson ->
                        lesson.color = Methods.setColor(lesson.subjectType)
                        lesson.subjectType =
                            resources.getString(Methods.returnFullNameOfTheItemType(lesson.subjectType))
                    }
                    exams
                } else {
                    emptyList()
                }
            }
            netUtil.setNetConnection(false)
        } catch (e: Exception) {
            netUtil.setNetConnection(false)
            return emptyList()
        }

        return emptyList()
    }

    override suspend fun getWeekTimeTable(): List<List<CellClass>> {

        if (netUtil.isNetConnection()) {
            val dayOfWeek = DateManager.getArrayOfWeekDate()
            try {
                mainParam.value?.let {
                    val response = Common.retrofitService.getAggregate(dayOfWeek, it.name)
                    if (response.isSuccessful) {
                        if (!response.body().isNullOrEmpty()) {
                            return replaceColomns(response.body()!!, dayOfWeek)
                        }
                    }
                }
            } catch (e: Exception) {
                return emptyList()
            }
        } else {
            netUtil.setNetConnection(false)
            return repository.getTimeTableFromStorage()
        }
        return repository.getTimeTableFromStorage()
    }

    private fun replaceColomns(
        list: Map<String, List<CellClass>>,
        dayOfWeekList: ArrayList<String>
    )
            : List<List<CellClass>> {
        val newList = mutableListOf<List<CellClass>>()
        dayOfWeekList.forEach { date ->
            list.get(date)?.let {

                it.forEach { lesson ->
                    lesson.color = Methods.setColor(lesson.subjectType)
                    lesson.subjectType =
                        resources.getString(Methods.returnFullNameOfTheItemType(lesson.subjectType))
                }
                newList.add(it)
            }
        }


        return newList
    }


    override suspend fun getListOfGroups() {
        var list = ArrayList<MainParam>()
        try {
            val restResponse = Common.retrofitService.getGroupsParamsList()
            if (restResponse.isSuccessful) {
                restResponse.body()?.forEach {
                    list.add(MainParam(it, false))
                }
            }
        } catch (e: Exception) {
            netUtil.setNetConnection(false)
            list = arrayListOf()
        }


        listOfMainParam.postValue(list)
    }

    override suspend fun getListOfTeachers() {
        var list = ArrayList<MainParam>()
        try {
            val restResponse = Common.retrofitService.getTeachersParamsList()
            if (restResponse.isSuccessful) {
                restResponse.body()?.forEach {
                    if (it.trim().isNotEmpty())
                        list.add(MainParam(it, false))
                }
            }
        } catch (e: Exception) {
            netUtil.setNetConnection(false)
            list = arrayListOf()
        }


        listOfMainParam.postValue(list)
    }

    override suspend fun getListOfAudWorkload(date: String): List<List<CellClass>> {

        return try {
            val restResponse = Common.retrofitService.getAudWorkload(date)
            if (restResponse.isSuccessful)
                restResponse.body() ?: listOf() else listOf()
        } catch (e: Exception) {
            netUtil.setNetConnection(false)
            listOf()
        }

    }

    override suspend fun getTeacherWorkload(name: String): Map<String, Map<String, Map<String, Int>>> {
        return try {
            val restResponse = Common.retrofitService.getTeacherWorkload(name)
            if (restResponse.isSuccessful)
                restResponse.body() ?: mapOf() else mapOf()
        } catch (e: Exception) {
            netUtil.setNetConnection(false)
            mapOf()
        }
    }

    override suspend fun getCafTimeTable(date: String, id: String): Map<String, List<CellClass>> {
        return try {
            val restResponse = Common.retrofitService.getCafTimeTable(date, id)
            if (restResponse.isSuccessful)
                restResponse.body() ?: mapOf() else mapOf()
        } catch (e: Exception) {
            netUtil.setNetConnection(false)
            mapOf()
        }
    }

    override fun getNewListOfMainParam(): MutableLiveData<ArrayList<MainParam>> {
        return listOfMainParam
    }

    override fun updateFavoriteParamList(newMainParam: MainParam) {
        val list = (listOfFavoriteMainParam.value?.toMutableList()
            ?: mutableListOf()) as ArrayList<MainParam>
        with(list) {

            if (newMainParam !in this) {
                this.add(0, newMainParam)
            } else {
                val index = this.indexOf(newMainParam)
                val item = this[index]
                this[index] = this[0]
                this[0] = item
            }
            // Если количество максимально
            if (this.size == 6) {
                this.removeLast()
            }
        }
        listOfFavoriteMainParam.value = list
    }

    override fun getDepartment(): List<String> {
        // Функция для получения данных с сервера
        val masOfDepartment = mutableListOf<String>()

        return masOfDepartment
    }

    override fun getNextMainParam(): String {
        if (mainParam.value != null) {
            val index = listOfFavoriteMainParam.value!!.indexOf(mainParam.value!!)
            val masLen = listOfFavoriteMainParam.value!!.size
            mainParam.value = listOfFavoriteMainParam.value!![(index + 1) % masLen]
            moveItemInFavoriteMainParam(mainParam.value!!)
        }
        return mainParam.value.toString()
    }

    override fun moveItemInFavoriteMainParam(param: MainParam): ArrayList<MainParam> {
        if (listOfFavoriteMainParam.value != null) {
            if (listOfFavoriteMainParam.value!!.size > 1) {
                val list = listOfFavoriteMainParam.value?.toList() as ArrayList
                with(list) {
                    if (this.size != 1) {
                        val itemIndex = this.indexOf(param)
                        for (item in itemIndex downTo 1) {
                            this[item] = this[item - 1]
                        }
                        this[0] = param
                    }
                    listOfFavoriteMainParam.value = list
                }
            }
        }


        return listOfFavoriteMainParam.value ?: ArrayList()
    }

    override suspend fun getListOfDetailedWorkload(
        month: String,
        department: String,
        year: String,
        mainParam: String
    ): Map<String, List<CellClass>> {
        return try {
            val restResponse =
                Common.retrofitService.getDetailedWorkload(month, department, year, mainParam)
            if (restResponse.isSuccessful)
                restResponse.body() ?: mapOf() else mapOf()
        } catch (e: Exception) {
            netUtil.setNetConnection(false)
            mapOf()
        }
    }

    override fun checkFirstBeginning(): Boolean {
        return !listOfFavoriteMainParam.value.isNullOrEmpty()
    }

    override fun clearListOfMainParam() {
        listOfMainParam.value?.clear()
    }
}

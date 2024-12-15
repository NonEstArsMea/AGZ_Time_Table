package com.NonEstArsMea.agz_time_table.data

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.net.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.net.retrofit.Common
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.NonEstArsMea.agz_time_table.util.Methods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeTableRepositoryImpl @Inject constructor(
    private val resources: Resources,
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
            mainParam.postValue(newMainParam)
        }
    }

    override fun setListOfFavoriteMainParam(list: ArrayList<MainParam>) {
        listOfFavoriteMainParam.value = list
    }

    override fun setWeekTimeTable(list: LiveData<List<List<CellClass>>>) {
        weekTimeTable = list as MutableLiveData<List<List<CellClass>>>
    }


    override suspend fun getExams(): List<CellClass> {
        val response = Common.retrofitService.getExams(mainParam.value!!.name)
        if (response.isSuccessful) {
            return if (response.body() != null) {
                val exams = response.body()!!
                exams.forEach { lesson ->
                    lesson.color = Methods.setColor(lesson.subjectType!!)
                    lesson.subjectType =
                        resources.getString(Methods.returnFullNameOfTheItemType(lesson.subjectType!!))
                }
                exams
            } else {
                emptyList()
            }
        }
        return emptyList()
    }


    override suspend fun getWeekTimeTable(): List<List<CellClass>> {
        val dayOfWeek = DateManager.getArrayOfWeekDate()

        mainParam.value?.let {
            val response = Common.retrofitService.getAggregate(dayOfWeek, it.name)
            Log.e("responce", response.body().toString())
            if (response.isSuccessful) {
                if (response.body() != null) {

                    return replaceColomns(response.body()!!, dayOfWeek)
                } else {
                    return emptyList()
                }
            } else {
                return emptyList()
            }
        }
        return emptyList()

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
                    lesson.color = Methods.setColor(lesson.subjectType!!)
                    lesson.subjectType =
                        resources.getString(Methods.returnFullNameOfTheItemType(lesson.subjectType!!))
                }
                newList.add(it)
            }
        }


        return newList
    }


    override suspend fun getListOfMainParam() {
        val list = ArrayList<MainParam>()

        val restResponce = Common.retrofitService.getMainParamsList()
        if (restResponce.isSuccessful) {
            restResponce.body()?.forEach {
                list.add(MainParam(it, false))
            }
        }


        listOfMainParam.postValue(list)
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

        val masOfDepartment = mutableListOf<String>()

        return masOfDepartment
    }

    override suspend fun getDepartmentTimeTable(
        departmentId: String,
        date: String
    ): List<List<CellClass>> = withContext(Dispatchers.Default) {
        val list = mutableListOf<List<CellClass>>()

        return@withContext list
    }

    companion object {
        const val NUMBER_OF_GROUP = 0
        const val NUMBER_OF_DAY = 2
        const val NUMBER_OF_LESSON = 3
        const val NUMBER_OF_AUD = 4
        const val NUMBER_OF_TEACHER = 7
        const val NUMBER_OF_CAFID = 8
        const val NUMBER_OF_SUBJECT = 9
        const val NUMBER_OF_SUBJ_TYPE = 10
        const val NUMBER_OF_DATE = 11
        const val NUMBER_OF_THEMAS = 14

    }

}

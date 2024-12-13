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
    private val dataRepositoryImpl: DataRepositoryImpl,
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


    override suspend fun getExams(mainParam: String): ArrayList<CellClass> =
        withContext(Dispatchers.Default) {
            val data = dataRepositoryImpl.getContent()
            val csvParser = CSVParser(
                data.reader(), CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .withDelimiter(';')
            )
            val listTT = ArrayList<CellClass>()
            var currentExam = CellClass(noEmpty = false)
            var lastDate = ""
            var lastSubject = ""
            for (line in csvParser) {
                val group = line.get(NUMBER_OF_GROUP)
                val aud = line.get(4)
                val name = line.get(7)
                val departmentId = line.get(8)
                val subject = line.get(9)
                val subj_type = line.get(10)
                val date = line.get(11).replace('.', '-')
                val themas = line.get(14)
                if ((mainParam == group) or ((mainParam == name)) and (Methods.validExams(subj_type))) {
                    if ((lastDate != date) or (lastSubject != subject)) {
                        listTT.add(currentExam)
                        currentExam = CellClass(noEmpty = true)
                        lastDate = date
                        lastSubject = subject
                    }
                    currentExam.apply {
                        if (teacher == null) {
                            teacher = name
                            studyGroup = group
                            classroom = aud
                            this.subject = subject
                            subjectNumber = listTT.size
                            subjectType = "$themas ${
                                resources.getString(
                                    Methods.returnFullNameOfTheItemType(subj_type)
                                )
                            }"
                            color = Methods.setColor(subj_type)
                            noEmpty = true
                            this.date = date
                            startTime = getStartTime(number = line.get(NUMBER_OF_LESSON).toInt())
                            endTime = getEndTime(number = line.get(NUMBER_OF_LESSON).toInt())
                            department = departmentId
                        } else {
                            if (name !in teacher!!) {
                                teacher += "\n${name}"
                            }
                            if (aud !in classroom!!) {
                                classroom += "\n${aud}"
                            }
                        }
                    }

                }

            }
            if (listTT.isEmpty()) {
                return@withContext listTT
            } else {
                listTT.removeAt(0)
                listTT.add(currentExam)
                return@withContext listTT
            }

        }



    override suspend fun getWeekTimeTable(): List<List<CellClass>> {
        val dayOfWeek = DateManager.getArrayOfWeekDate()

        mainParam.value?.let {
            val response = Common.retrofitService.getAggregate(dayOfWeek, it.name)
            Log.e("responce", response.body().toString())
            if(response.isSuccessful){
                if (response.body() != null){

                    return replaceColomns(dayOfWeek, response.body()!!)
                }else{
                    return emptyList()
                }
            }else{
                return emptyList()
            }
        }
        return emptyList()

    }

    fun replaceColomns(dayOfWeek: ArrayList<String>,
                       list: List<List<CellClass>>): List<List<CellClass>>{
        val newList = mutableListOf<List<CellClass>>()
        dayOfWeek.forEach {
            for(day in list){
                for(b in day){
                    if(b.date == it){
                        newList.add(day)
                        break
                    }
                }
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

    private fun getStartTime(number: Int): String {
        return when (number) {
            1 -> "09:00"
            2 -> "10:45"
            3 -> "12:30"
            4 -> "14:45"
            5 -> "16:25"
            else -> {
                throw Exception("Unknown EndStart - $number")
            }
        }
    }

    private fun getEndTime(number: Int?): String {
        return when (number) {
            1 -> "10:30"
            2 -> "12:15"
            3 -> "14:00"
            4 -> "16:15"
            5 -> "17:55"
            else -> {
                throw Exception("Unknown EndTime - $number")
            }
        }
    }

    private fun wordEnding(number: Int): String {
        return when (number) {
            1 -> resources.getString(R.string.pair)
            2, 3, 4 -> resources.getString(R.string.pairs)
            else -> {
                throw Exception("Unknown Number - $number")
            }
        }
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
        val data = dataRepositoryImpl.getContent()
        val csvParser = CSVParser(
            data.reader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withDelimiter(';')
        )

        val masOfDepartment = mutableListOf<String>()
        for (line in csvParser) {
            if (line.get(NUMBER_OF_CAFID) !in masOfDepartment) {
                masOfDepartment.add(line.get(7))
            }
        }
        masOfDepartment.sort()
        return masOfDepartment
    }

    override suspend fun getDepartmentTimeTable(
        departmentId: String,
        date: String
    ): List<List<CellClass>> = withContext(Dispatchers.Default) {
        val data = dataRepositoryImpl.getContent()
        val csvParser = CSVParser(
            data.reader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withDelimiter(';')
        )
        val listOfTeachers = emptyList<String>().toMutableList()
        for (line in csvParser) {
            if (line.get(NUMBER_OF_CAFID) == departmentId &&
                (line.get(NUMBER_OF_TEACHER) !in listOfTeachers)
            ) {
                listOfTeachers.add(line.get(6))
            }
        }

        listOfTeachers.sort()

        val list = mutableListOf<List<CellClass>>()
        repeat((1..list.size).count()) { _ ->
            list.add(emptyList<CellClass>().toMutableList())
        }
        for (line in csvParser) {
            for (teacher in listOfTeachers) {
                if (line.get(NUMBER_OF_CAFID) == departmentId &&
                    line.get(NUMBER_OF_DATE).replace('.', '-') == date &&
                    line.get(NUMBER_OF_TEACHER) == teacher
                ) {

//                group = line.get(0)
//                les = line.get(2).toInt() - 1
//                aud = line.get(3)
//                name = line.get(6)
//                _subject = line.get(8)
//                subj_type = line.get(9)
//                departmentId = line.get(7)
                    list[listOfTeachers.indexOf(teacher)].toMutableList().add(
                        CellClass(
                            noEmpty = true,
                            teacher = line.get(NUMBER_OF_TEACHER).toString(),
                            subject = line.get(NUMBER_OF_SUBJECT).toString(),
                            date = date,
                            subjectNumber = line.get(NUMBER_OF_LESSON).toInt()
                        )
                    )
                }
            }

        }
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

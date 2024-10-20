package com.NonEstArsMea.agz_time_table.data

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.net.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.storage.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.NonEstArsMea.agz_time_table.util.Methods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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

    override suspend fun preparationData(
        dayOfWeek: String,
        mainParam: String,
    ): ArrayList<CellClass> = withContext(Dispatchers.Default) {

        val data = dataRepositoryImpl.getContent()
        val csvParser = CSVParser(
            data.reader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withDelimiter(';')
        )
        val listOfLes = mutableListOf<Int>()
        var listTT = ArrayList<CellClass>()
        for (a in 1..5) {
            listTT.add(
                CellClass(
                    noEmpty = false,
                )
            )
        }
        var group: String
        var les: Int
        var aud: String
        var name: String
        var _subject: String
        var subj_type: String
        var departmentId: String
        var _date: String
        var themas: String

        for (line in csvParser) {
            group = line.get(0)
            les = line.get(3).toInt() - 1
            aud = line.get(4)
            name = line.get(7)
            _subject = line.get(9)
            subj_type = line.get(10)
            departmentId = line.get(8)
            _date = line.get(11).replace('.', '-')
            themas = line.get(14)
            if ((_date == dayOfWeek) and ((mainParam == group) or (mainParam == aud) or (mainParam == name))) {
                listTT[les].apply {
                    if (teacher == null) {
                        teacher = name
                        studyGroup = group
                        classroom = aud
                        subject = _subject
                        subjectNumber = les + 1
                        subjectType = "$themas ${
                            resources.getString(
                                Methods.returnFullNameOfTheItemType(subj_type)
                            )
                        }"
                        color = Methods.setColor(subj_type)
                        noEmpty = true
                        date = _date
                        department = departmentId
                        startTime = getStartTime(number = les + 1)
                        endTime = getEndTime(number = les + 1)
                        listOfLes.add(les + 1)
                    } else {
                        if (name !in listTT[les].teacher!!) {
                            listTT[les].teacher += "\n${name}"
                        }
                        if (aud !in listTT[les].classroom!!) {
                            listTT[les].classroom += "\n${aud}"
                        }
                    }
                }
            }

        }
        // Фильтрация списка
        listTT = listTT.filter {
            it.noEmpty
        } as ArrayList<CellClass>

        Log.e("tag2", mainParam.toString())

        return@withContext setBreakCell(listOfLes, listTT)
    }

    private fun setBreakCell(
        listOfLes: MutableList<Int>,
        listTT: ArrayList<CellClass>,
    ): ArrayList<CellClass> {
        var listOfLes1 = listOfLes
        listOfLes1 = listOfLes1.sorted().toMutableList()
        var lessonOffset = 0
        if (!listTT.isEmpty()) {
            // Проверка первого элемента
            if (listTT[0].subjectNumber != 1) {
                val endTime = getStartTime(listTT[0].subjectNumber!!)
                val countOfPairs = listTT[0].subjectNumber!! - 1
                val wordEnd = wordEnding(listTT[0].subjectNumber!! - 1)
                listTT.add(
                    0,
                    CellClass(
                        text = "9:00 - $endTime     $countOfPairs $wordEnd",
                        noEmpty = true,
                        viewSize = 50
                    )
                )
                // Нужно для вставки по индексу
                lessonOffset += 1
            }
            if (listOfLes1.size > 1) {
                for (a in 0 until listOfLes1.size - 1) {
                    // Разность пар
                    val diff = (listOfLes1[a + 1] - listOfLes1[a])

                    if (diff > 1) {

                        val endTime =
                            getEndTime(listTT[a + lessonOffset].subjectNumber!!)
                        val startTime =
                            getStartTime(listTT[a + lessonOffset + 1].subjectNumber!!)
                        val wordEnd = wordEnding(diff - 1)
                        listTT.add(
                            index = a + lessonOffset + 1,
                            element = CellClass(
                                text = "${endTime} - ${startTime}   ${diff - 1} $wordEnd",
                                noEmpty = true,
                                viewSize = 60
                            )
                        )
                        lessonOffset += 1
                    } else if (listOfLes1[a + 1] == 4) {
                        listTT.add(
                            index = a + lessonOffset + 1,
                            element = CellClass(
                                text = resources.getString(R.string.break_45),
                                noEmpty = true,
                                viewSize = 50
                            )
                        )
                        lessonOffset += 1
                    } else {
                        listTT.add(
                            index = a + lessonOffset + 1,
                            element = CellClass(
                                text = resources.getString(R.string.break_15),
                                noEmpty = true,
                                viewSize = 16
                            )
                        )
                        lessonOffset += 1
                    }
                }
            }
        } else {
            listTT.add(
                CellClass(
                    text = resources.getString(R.string.no_school),
                    noEmpty = true,
                    viewSize = 20
                )
            )
        }

        return listTT
    }


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

        return mainParam.value?.let { mainParamValue ->
            try {
                coroutineScope {
                    dayOfWeek.map { day ->
                        async {
                            preparationData(day, mainParamValue.name)
                        }
                    }.awaitAll()
                }
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }


    override fun getListOfMainParam() {
        val data = dataRepositoryImpl.getContent()
        val csvParser = CSVParser(
            data.reader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withDelimiter(';')
        )

        val listGroup = ArrayList<MainParam>()
        val listAud = ArrayList<MainParam>()
        val listName = ArrayList<MainParam>()

        for (line in csvParser) {
            val group = line.get(NUMBER_OF_GROUP)
            val aud = line.get(NUMBER_OF_AUD)
            val name = line.get(NUMBER_OF_TEACHER)

            if (listGroup.none { it.name == group }) {
                listGroup.add(MainParam(group, false))
            }
            if (listAud.none { it.name == aud }) {
                listAud.add(MainParam(aud, false))
            }
            if (listName.none { it.name == name }) {
                listName.add(MainParam(name, false))
            }

        }



        listGroup.sortBy { it.name }
        listGroup.forEachIndexed { index, mainParam -> mainParam.position = index }

        listAud.sortBy { it.name }
        listAud.forEachIndexed { index, mainParam -> mainParam.position = index }

        listName.sortBy { it.name }
        listName.forEachIndexed { index, mainParam -> mainParam.position = index }

        listOfMainParam.value = ArrayList<MainParam>().apply {
            addAll(listGroup)
            addAll(listAud)
            addAll(listName)
        }

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
        for (line in csvParser){
            if(line.get(NUMBER_OF_CAFID) !in masOfDepartment){
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
                (line.get(NUMBER_OF_TEACHER) !in listOfTeachers)) {
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

    companion object{
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

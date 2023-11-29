package com.NonEstArsMea.agz_time_table.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.Methods
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser


object TimeTableRepositoryImpl : TimeTableRepository {

    private var weekTimeTable = MutableLiveData<List<List<CellApi>>>()
    private var mainParam = MutableLiveData<MainParam>()
    private var listOfMainParam = MutableLiveData<ArrayList<MainParam>>()
    private var listOfFavoriteMainParam = MutableLiveData<ArrayList<MainParam>>()
    private var theme = MutableLiveData<Int>()

    override suspend fun preparationData(
        data: String,
        dayOfWeek: String,
        mainParam: String,
        context: Context
    ): ArrayList<CellApi> = withContext(Dispatchers.Default) {

        val _data = DataRepositoryImpl.getContent()
        val csvParser = CSVParser(
            _data.reader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withDelimiter(';')
        )
        var listOfLes = mutableListOf<Int>()
        var listTT = ArrayList<CellApi>()
        for (a in 1..5) {
            listTT.add(
                CellApi(
                    noEmpty = false,
                )
            )
        }
        var l = 0
        for (line in csvParser) {
            l++

            val group = line.get(0)
            val les = line.get(2).toInt() - 1
            val aud = line.get(3)
            val name = line.get(6)
            val _subject = line.get(8)
            val subj_type = line.get(9)
            val departmentId = line.get(7)
            val _date = line.get(10).replace('.', '-')
            val themas = line.get(12)
            if ((_date == dayOfWeek) and ((mainParam == group) or (mainParam == aud) or (mainParam == name))) {
                listTT[les].apply {
                    if (teacher == null) {
                        teacher = name
                        studyGroup = group
                        classroom = aud
                        subject = _subject
                        subjectNumber = les + 1
                        subjectType = "$themas ${Methods.replaceText(subj_type)}"
                        color = Methods.setColor(subj_type)
                        noEmpty = true
                        date = _date
                        department = departmentId
                        startTime = getStartTime(number = les + 1)
                        endTime = getEndTime(number = les + 1)
                        listOfLes.add(les + 1)
                    }else {
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
        } as ArrayList<CellApi>

        listOfLes = listOfLes.sorted().toMutableList()
        var lessonOffset = 0
        // Список может быть пустым
        if (!listTT.isEmpty()) {
            // ПРоверка первого элемента
            if (listTT[0].subjectNumber != 1) {
                listTT.add(
                    0,
                    CellApi(
                        // 9.00 - 10.30     2 пары
                        text = "9:00 - ${getStartTime(listTT[0].subjectNumber!!)}     ${listTT[0].subjectNumber!! - 1} ${
                            wordEnding(
                                listTT[0].subjectNumber!! - 1,
                                context
                            )
                        }",
                        noEmpty = true,
                        viewSize = 50
                    )
                )
                // Нужно для вставки по индексу
                lessonOffset += 1
            }
            if (listOfLes.size > 1) {
                for (a in 0 until listOfLes.size - 1) {
                    // Разность пар
                    val diff = (listOfLes[a + 1] - listOfLes[a])

                    if (diff > 1) {

                        val endTime =
                            getEndTime(listTT[a + lessonOffset].subjectNumber!!)
                        val startTime =
                            getStartTime(listTT[a + lessonOffset + 1].subjectNumber!!)
                        listTT.add(
                            index = a + lessonOffset + 1,
                            element = CellApi(
                                text = "${endTime} - ${startTime}   ${diff - 1} ${wordEnding(diff - 1, context)}",
                                noEmpty = true,
                                viewSize = 60
                            )
                        )
                        lessonOffset += 1
                    } else if (listOfLes[a + 1] == 4) {
                        listTT.add(
                            index = a + lessonOffset + 1,
                            element = CellApi(
                                text = context.getString(R.string.break_45),
                                noEmpty = true,
                                viewSize = 50
                            )
                        )
                        lessonOffset += 1
                    } else {
                        listTT.add(
                            index = a + lessonOffset + 1,
                            element = CellApi(
                                text = context.getString(R.string.break_15),
                                noEmpty = true,
                                viewSize = 16
                            )
                        )
                        lessonOffset += 1
                    }
                }
            }
        } else {
            listTT.add(CellApi(text = context.getString(R.string.no_school), noEmpty = true, viewSize = 20))
            return@withContext listTT
        }
        return@withContext listTT
    }


    override fun getMainParam(): MutableLiveData<MainParam> {
        return mainParam
    }

    override fun getArrayOfFavoriteMainParam(): MutableLiveData<ArrayList<MainParam>> {
        return listOfFavoriteMainParam
    }

    override fun getArrayOfWeekTimeTable(): MutableLiveData<List<List<CellApi>>> {
        return weekTimeTable
    }

    override fun getTheme(): MutableLiveData<Int> {
        return theme
    }

    override fun setMainParam(newMainParam: MainParam) {
        if (mainParam.value != newMainParam) {
            mainParam.postValue(newMainParam)
        }
    }

    override fun setListOfFavoriteMainParam(list: ArrayList<MainParam>) {
        listOfFavoriteMainParam.value = list
    }

    override fun setWeekTimeTable(list: ArrayList<ArrayList<CellApi>>) {
        weekTimeTable.value = list
    }

    override fun setTheme(newTheme: Int) {

        theme.value = newTheme
    }

    override suspend fun getExams(data: String, mainParam: String): ArrayList<CellApi> =
        withContext(Dispatchers.Default) {
            val csvParser = CSVParser(
                data.reader(), CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .withDelimiter(';')
            )
            val listTT = ArrayList<CellApi>()
            var currentExam = CellApi(noEmpty = false)
            var lastDate = ""
            var lastSubject = ""
            for (line in csvParser) {
                val group = line.get(0)
                val aud = line.get(3)
                val name = line.get(6)
                val departmentId = line.get(7)
                val subject = line.get(8)
                val subj_type = line.get(9)
                val date = line.get(10).replace('.', '-')
                val themas = line.get(12)
                if ((mainParam == group) or ((mainParam == name)) and (Methods.validExams(subj_type))) {
                    if ((lastDate != date) or (lastSubject != subject)) {
                        listTT.add(currentExam)
                        currentExam = CellApi(noEmpty = true)
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
                            subjectType = "$themas ${Methods.replaceText(subj_type)}"
                            color = Methods.setColor(subj_type)
                            noEmpty = true
                            this.date = date
                            startTime = getStartTime(number = line.get(2).toInt())
                            endTime = getEndTime(number = line.get(2).toInt())
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
            listTT.removeAt(0)
            listTT.add(currentExam)
            return@withContext listTT
        }


    override suspend fun getWeekTimeTable(newData: String, context: Context): List<List<CellApi>> {
        val dayOfWeek = DateRepositoryImpl.getArrayOfWeekDate()
        return mainParam.value?.let { mainParamValue ->
            try {
                coroutineScope {
                    dayOfWeek.map { day ->
                        async {
                            preparationData(newData, day, mainParamValue.name, context)
                        }
                    }.awaitAll()
                }
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }


    override fun getListOfMainParam() {
        val _data = DataRepositoryImpl.getContent()
        val csvParser = CSVParser(
            _data.reader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withDelimiter(';')
        )

        val listGroup = ArrayList<MainParam>()
        val listAud = ArrayList<MainParam>()
        val listName = ArrayList<MainParam>()

        for (line in csvParser) {
            val Group = line.get(0)
            val Aud = line.get(3)
            val Name = line.get(6)

            if (listGroup.none { it.name == Group }) {
                listGroup.add(MainParam(Group, false))
            }
            if (listAud.none { it.name == Aud }) {
                listAud.add(MainParam(Aud, false))
            }
            if (listName.none { it.name == Name }) {
                listName.add(MainParam(Name, false))
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

    private fun wordEnding(number: Int, context: Context): String {
        return when (number) {
            1 -> context.getString(R.string.pair)
            2, 3, 4 -> context.getString(R.string.pairs)
            else -> {
                throw Exception("Unknown Number - $number")
            }
        }
    }

    override fun updateFavoriteParamList(newMainParam: MainParam) {
        val list = (listOfFavoriteMainParam.value?.toMutableList() ?: mutableListOf()) as ArrayList<MainParam>

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


}

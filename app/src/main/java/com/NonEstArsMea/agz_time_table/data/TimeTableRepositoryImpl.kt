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
        dayOfWeek: String,
        mainParam: String,
        context: Context
    ): ArrayList<CellApi> = withContext(Dispatchers.Default) {

        val data = DataRepositoryImpl.getContent()
        val csvParser = CSVParser(
            data.reader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withDelimiter(';')
        )
        val listOfLes = mutableListOf<Int>()
        var listTT = ArrayList<CellApi>()
        for (a in 1..5) {
            listTT.add(
                CellApi(
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
            les = line.get(2).toInt() - 1
            aud = line.get(3)
            name = line.get(6)
            _subject = line.get(8)
            subj_type = line.get(9)
            departmentId = line.get(7)
            _date = line.get(10).replace('.', '-')
            themas = line.get(12)
            if ((_date == dayOfWeek) and ((mainParam == group) or (mainParam == aud) or (mainParam == name))) {
                listTT[les].apply {
                    if (teacher == null) {
                        teacher = name
                        studyGroup = group
                        classroom = aud
                        subject = _subject
                        subjectNumber = les + 1
                        subjectType = "$themas ${context.getString(Methods.returnFullNameOfTheItemType(subj_type))}"
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

        return@withContext setBreakCell(listOfLes, listTT, context)
    }

    private fun setBreakCell(
        listOfLes: MutableList<Int>,
        listTT: ArrayList<CellApi>,
        context: Context
    ): ArrayList<CellApi> {
        var listOfLes1 = listOfLes
        listOfLes1 = listOfLes1.sorted().toMutableList()
        var lessonOffset = 0
        if (!listTT.isEmpty()) {
            // Проверка первого элемента
            if (listTT[0].subjectNumber != 1) {
                val endTime = getStartTime(listTT[0].subjectNumber!!)
                val countOfPairs = listTT[0].subjectNumber!! - 1
                val wordEnd = wordEnding(listTT[0].subjectNumber!! - 1, context)
                listTT.add(
                    0,
                    CellApi(
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
                        val wordEnd = wordEnding(diff - 1, context)
                        listTT.add(
                            index = a + lessonOffset + 1,
                            element = CellApi(
                                text = "${endTime} - ${startTime}   ${diff - 1} $wordEnd",
                                noEmpty = true,
                                viewSize = 60
                            )
                        )
                        lessonOffset += 1
                    } else if (listOfLes1[a + 1] == 4) {
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
            listTT.add(
                CellApi(
                    text = context.getString(R.string.no_school),
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

    override suspend fun getExams(mainParam: String, context: Context): ArrayList<CellApi> =
        withContext(Dispatchers.Default) {
            val _data = DataRepositoryImpl.getContent()
            val csvParser = CSVParser(
                _data.reader(), CSVFormat.DEFAULT
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
                            subjectType = "$themas ${context.getString(Methods.returnFullNameOfTheItemType(subj_type))}"
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


    override suspend fun getWeekTimeTable(context: Context): List<List<CellApi>> {
        val dayOfWeek = DateRepositoryImpl.getArrayOfWeekDate()
        return mainParam.value?.let { mainParamValue ->
            try {
                coroutineScope {
                    dayOfWeek.map { day ->
                        async {
                            preparationData(day, mainParamValue.name, context)
                        }
                    }.awaitAll()
                }
            } catch (e: Exception) {
                emptyList()
            }
        } ?: emptyList()
    }


    override fun getListOfMainParam() {
        val data = DataRepositoryImpl.getContent()
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
            val group = line.get(0)
            val aud = line.get(3)
            val name = line.get(6)

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

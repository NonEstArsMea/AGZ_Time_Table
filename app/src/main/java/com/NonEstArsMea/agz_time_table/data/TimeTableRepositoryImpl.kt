package com.NonEstArsMea.agz_time_table.data

import android.util.Log
import com.NonEstArsMea.agz_time_table.domain.Methods
import com.NonEstArsMea.agz_time_table.domain.TimeTableRepository
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser


object TimeTableRepositoryImpl: TimeTableRepository {

    private var weekTimeTable = ArrayList<ArrayList<CellApi>>(6 )

    override suspend fun preparationData(
        data: String,
        dayOfWeek: String,
        mainParam:String): ArrayList<CellApi> = withContext(Dispatchers.Default) {


        val csvParser =  CSVParser(
            data.reader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withDelimiter(';')
        )
        var lessonOffset = 1
        var listOfLes = mutableListOf<Int>()
        var listTT = ArrayList<CellApi>()
        for(a in 1..5){
               listTT.add(CellApi(
                   noEmpty = false,
               ))
        }
        try {
            for (line in csvParser) {
                val Group = line.get(0)
                val Les = line.get(2).toInt() - 1
                val Aud = line.get(3)
                val Name = line.get(6)
                val Subject = line.get(8)
                val Subj_type = line.get(9)
                val Date = line.get(10)
                if ((Date == dayOfWeek) and ((mainParam == Group) or (mainParam == Aud) or (mainParam == Name))) {
                    if (listTT[Les].teacher == null) {
                        listTT[Les].teacher = Name
                        listTT[Les].studyGroup = Group
                        listTT[Les].classroom = Aud
                        listTT[Les].subject = Subject
                        listTT[Les].subjectNumber = Les + 1
                        listTT[Les].subjectType = Methods().replaceText(Subj_type)
                        listTT[Les].color = Methods().setColor(Subj_type)
                        listTT[Les].noEmpty = true
                        listTT[Les].date = Date
                        listTT[Les].startTime = getStartTime(number = Les + 1)
                        listTT[Les].endTime = getEndTime(number = Les + 1)
                        listOfLes.add(Les + 1)
                    } else {
                        if (Name !in listTT[Les].teacher!!) {
                            listTT[Les].teacher += "\n${Name}"
                        }
                        if (Aud !in listTT[Les].classroom!!) {
                            listTT[Les].classroom += "\n${Aud}"
                        }
                    }
                }

            }
        }catch (e: Exception){
            Log.e("TTRI", e.toString())
        }
        // Фильтрация списка
        listTT = listTT.filter {
            it.noEmpty
        } as ArrayList<CellApi>

        listOfLes = listOfLes.sorted().toMutableList()

        // Список может быть пустым
        if(!listTT.isEmpty()) {
            // ПРоверка первого элемента
            if (listTT[0].subjectNumber != 1) {
                listTT.add(
                    0,
                    CellApi(
                        text = "9:00 - ${getStartTime(listTT[0].subjectNumber!!)}     ${listTT[0].subjectNumber!!-1} ${wordEnding(listTT[0].subjectNumber!!-1)}",
                        noEmpty = true,
                        viewSize = 50
                    )
                )
                // Нужно для вставки по индексу
                lessonOffset += 1
            }
            Log.e("TTRI", "${listTT.size.toString()} ${listOfLes.toString()}")
            if(listOfLes.size > 1) {
                for (a in 0 until listOfLes.size-1) {
                    val diff = (listOfLes[a + 1] - listOfLes[a])
                    if (diff > 1){
                        listTT.add(
                            index = a + lessonOffset,
                            element = CellApi(
                                text = "${getEndTime(listTT[a].subjectNumber!!)}  - ${getStartTime(listTT[a + lessonOffset].subjectNumber!!)}     ${diff-1} ${wordEnding(diff-1)}",
                                noEmpty = true,
                                viewSize = 60
                            )
                        )
                        lessonOffset += 1
                    }else if(listOfLes[a+1] == 4){
                        listTT.add(
                            index = a + lessonOffset,
                            element = CellApi(
                                text = "Перерыв на обед - 45 минут",
                                noEmpty = true,
                                viewSize = 50
                            )
                        )
                        lessonOffset += 1
                    }else{
                        listTT.add(
                            index = a + lessonOffset,
                            element = CellApi(
                                text = "Перерыв 15 минут",
                                noEmpty = true,
                                viewSize = 20
                            )
                        )
                        lessonOffset += 1
                    }
                }
            }
            Log.e("TTRI", "${listTT.size.toString()} ")
        }else{
            listTT.add(CellApi(text = "В этот день занятий нет.", noEmpty = true, viewSize = 20))
            return@withContext listTT
        }
        return@withContext listTT
    }



    override fun getLessonTimeTable(numberLesson: Int, dayOfWeek: Int): CellApi {
        return weekTimeTable[dayOfWeek][numberLesson]
    }


    override fun getDayTimeTable(dayOfWeek: Int): ArrayList<CellApi> {
        return weekTimeTable[dayOfWeek]
    }

    override suspend fun getWeekTimeTable(
        newData: String,
        dayOfWeek: ArrayList<String>,
        mainParam: String
    ): ArrayList<ArrayList<CellApi>> {
        weekTimeTable.clear()
        Log.e("AAA", weekTimeTable.toString())
        try {
            for (a in 1..6) {
                weekTimeTable.add(arrayListOf<CellApi>())
            }
            val arrDeferred = arrayListOf<Deferred<ArrayList<CellApi>>>()
            coroutineScope {
                for (a in 1..6) {
                    arrDeferred.add(async { preparationData(newData, dayOfWeek[a - 1], mainParam) })
                }

                for (a in 1..6) {
                    weekTimeTable[a - 1] = arrDeferred[a - 1].await()
                }
            }
        }catch(e: Exception) {
            Log.e("my-tag", e.toString())
        }

        return weekTimeTable
    }


    override fun getListOfMainParam(data: String, arrOfMainParams: ArrayList<MainParam>?): List<MainParam> {
        val csvParser =  CSVParser(
            data.reader(), CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withDelimiter(';')
        )

        var i = 0
        val listGroup = ArrayList<MainParam>()
        val listAud = ArrayList<MainParam>()
        val listName = ArrayList<MainParam>()
        var arr =  arrOfMainParams
        if(arr == null){
            arr = arrayListOf<MainParam>()
        }

        for (line in csvParser){
            val Group = line.get(0)
            val Aud = line.get(3)
            val Name = line.get(6)

            if(listGroup.none { it.name == Group}){
                if (arr.any { it.name == Group }) {
                    listGroup.add(MainParam(Group, true))
                } else {
                    listGroup.add(MainParam(Group, false))
                }
            }
            if(listAud.none { it.name == Aud}){
                if (arr.any { it.name == Aud }) {
                    listAud.add(MainParam(Aud, true))
                } else {
                    listAud.add(MainParam(Aud, false))
                }
            }
            if(listName.none { it.name == Name}){
                if (arr.any { it.name == Name }) {
                    listName.add(MainParam(Name, true))
                } else {
                    listName.add(MainParam(Name, false))
                }
            }

        }
        listGroup.sortBy { it.name }
        listGroup.forEach{ it.position = i++}
        listAud.sortBy { it.name }
        listAud.forEach{ it.position = i++}
        listName.sortBy { it.name }
        listName.forEach{ it.position = i++}
        return listGroup + listAud + listName
    }

    private fun getStartTime(number: Int): String{
        return when(number){
            1 -> "09:00"
            2 -> "10:45"
            3 -> "12:30"
            4 -> "14:45"
            5 -> "16:25"
            else->{
                ""
            }
        }
    }

    private fun getEndTime(number: Int): String{
        return when(number){
            1 -> "10:30"
            2 -> "12:15"
            3 -> "14:00"
            4 -> "16:15"
            5 -> "17:55"
            else->{
                ""
            }
        }
    }

    private fun wordEnding(number: Int): String{
        return when(number){
            1 -> "пара"
            2,3,4 -> "пары"
            else->{
                ""
            }
        }
    }

}

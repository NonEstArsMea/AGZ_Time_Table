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
        var listTT = ArrayList<CellApi>()
        for(a in 1..9){
               listTT.add(CellApi(
                   subject = null,
                   teacher = null,
                   classroom = null,
                   studyGroup = null,
                   date = null,
                   subjectType = null,
                   startTime = null,
                   endTime = null,
                   subjectNumber = null,
                   noEmpty = false,
                   text = null,
                   color = null,
                   viewType = null
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
                    if (listTT[Les*2].teacher == null) {
                        listTT[Les*2].teacher = Name
                        listTT[Les*2].studyGroup = Group
                        listTT[Les*2].classroom = Aud
                        listTT[Les*2].subject = Subject
                        listTT[Les*2].subjectNumber = Les
                        listTT[Les*2].subjectType = Methods().replaceText(Subj_type)
                        listTT[Les*2].color = Methods().setColor(Subj_type)
                        listTT[Les*2].noEmpty = true
                        listTT[Les*2].date = Date
                        listTT[Les*2].startTime = getStartTime(number = Les + 1)
                        listTT[Les*2].endTime = getEndTime(number = Les + 1)
                        if(Les != 0) {
                            listTT[Les * 2 - 1].text = "Перемена"
                            listTT[Les * 2 - 1].noEmpty = true
                        }
                    } else {
                        if (Name !in listTT[Les].teacher!!) {
                            listTT[Les*2].teacher += "\n${Name}"
                        }
                        if (Aud !in listTT[Les].classroom!!) {
                            listTT[Les*2].classroom += "\n${Aud}"
                        }
                    }
                }

            }
        }catch (e: Exception){
            Log.e("TTRI", e.toString())
        }
        listTT = listTT.filter {
            it.noEmpty
        } as ArrayList<CellApi>
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
            1 -> "09:40"
            2 -> "12:15"
            3 -> "14:00"
            4 -> "16:15"
            5 -> "17:55"
            else->{
                ""
            }
        }
    }

}

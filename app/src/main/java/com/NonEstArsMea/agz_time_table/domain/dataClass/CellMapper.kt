package com.NonEstArsMea.agz_time_table.domain.dataClass

class CellMapper {

    fun cellApiToUI(api: CellApi): Cell{
        return when{
            api.text != null -> BreakCell(
                text = api.text,
                viewType = 1,
                noEmpty = api.noEmpty!!)
            else -> LessonTimeTable(subject = api.subject,
                teacher = api.teacher,
                classroom = api.classroom,
                studyGroup = api.studyGroup,
                date = api.date,
                subjectType = api.subjectType,
                startTime = api.startTime,
                endTime = api.endTime,
                subjectNumber = api.subjectNumber,
                noEmpty = true,
                color = api.color!!,
                viewType = 0,
            )
        }
    }
}
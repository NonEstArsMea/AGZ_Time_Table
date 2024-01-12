package com.NonEstArsMea.agz_time_table.data.dataBase

import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass

class TimeTableMapper {
    fun mapEntityToDbModel(entity: CellClass): CellClassDbModel {
        return CellClassDbModel(
            text = entity.text,
            viewSize = entity.viewSize,
            viewType = entity.viewType,
            subjectType = entity.subjectType,
            id = 0,
            teacher = entity.teacher,
            subjectNumber = entity.subjectNumber,
            subject = entity.subject,
            studyGroup = entity.studyGroup,
            startTime = entity.startTime,
            lessonTheme = entity.lessonTheme,
            isGone = entity.isGone,
            endTime = entity.endTime,
            department = entity.department,
            date = entity.date,
            color = entity.color,
            classroom = entity.classroom,
            noEmpty = entity.noEmpty
        )
    }

    fun mapDbModelToEntity(dbModel: CellClassDbModel): CellClass {
        return CellClass(
            text = dbModel.text,
            viewSize = dbModel.viewSize,
            viewType = dbModel.viewType,
            subjectType = dbModel.subjectType,
            teacher = dbModel.teacher,
            subjectNumber = dbModel.subjectNumber,
            subject = dbModel.subject,
            studyGroup = dbModel.studyGroup,
            startTime = dbModel.startTime,
            lessonTheme = dbModel.lessonTheme,
            isGone = dbModel.isGone,
            endTime = dbModel.endTime,
            department = dbModel.department,
            date = dbModel.date,
            color = dbModel.color,
            classroom = dbModel.classroom,
            noEmpty = dbModel.noEmpty
        )
    }

    fun mapTimeTableDbModelToEntity(list: List<List<CellClassDbModel>>): List<List<CellClass>> {
        return list.map { week ->
            week.map { day ->
                mapDbModelToEntity(day)
            }
        }
    }
}
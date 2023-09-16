package com.NonEstArsMea.agz_time_table.domain.dataClass

import com.NonEstArsMea.agz_time_table.R

data class CellApi(
    var subject: String?,
    var teacher: String?,
    var classroom: String?,
    var studyGroup: String?,
    var date: String?,
    var subjectType: String?,
    var startTime: String?,
    var endTime: String?,
    var subjectNumber: Int?,
    var noEmpty: Boolean,
    var text: String?,
    var color: Int?,
    val viewType: Int?,
)

sealed class Cell
data class LessonTimeTable(
    var subject: String? = "123",
    var teacher: String? = "",
    var classroom: String? = "123",
    var studyGroup: String? = "123",
    var date: String? = "123",
    var subjectType: String? = "123",
    var startTime: String? = "123",
    var endTime: String? = "123",
    var subjectNumber: Int? = -1,
    var noEmpty: Boolean = true,
    var color: Int = R.color.yellow_fo_lessons_card,
    val viewType: Int = 0,
):Cell()
data class BreakCell(
    var text: String?,
    var noEmpty: Boolean = true,
    val viewType: Int = 1,
):Cell()
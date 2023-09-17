package com.NonEstArsMea.agz_time_table.domain.dataClass

import androidx.compose.ui.unit.dp
import com.NonEstArsMea.agz_time_table.R

data class CellApi(
    var subject: String? = null,
    var teacher: String?= null,
    var classroom: String?= null,
    var studyGroup: String?= null,
    var date: String?= null,
    var subjectType: String?= null,
    var startTime: String?= null,
    var endTime: String?= null,
    var subjectNumber: Int?= null,
    var noEmpty: Boolean,
    var text: String?= null,
    var color: Int?= null,
    val viewType: Int?= null,
    val viewSize: Int?= null,
)

sealed class Cell
data class LessonTimeTable(
    var subject: String?,
    var teacher: String?,
    var classroom: String?,
    var studyGroup: String?,
    var date: String?,
    var subjectType: String?,
    var startTime: String?,
    var endTime: String?,
    var subjectNumber: Int?,
    var noEmpty: Boolean = true,
    var color: Int = R.color.yellow_fo_lessons_card,
    val viewType: Int = 0,
):Cell()
data class BreakCell(
    var text: String?,
    var noEmpty: Boolean = true,
    val viewType: Int = 1,
    val viewSize: Int = 20,
):Cell()
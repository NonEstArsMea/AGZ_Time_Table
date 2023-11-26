package com.NonEstArsMea.agz_time_table.domain.dataClass

import android.os.Parcel
import android.os.Parcelable
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
    var lessonTheme: String? = null,
    var color: Int= R.color.yellow_fo_lessons_card,
    val viewType: Int?= null,
    val viewSize: Int?= null,
    var isGone: Boolean = true,
    var department: String? = null,
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
    var lessonTheme: String? = null,
    var color: Int = R.color.yellow_fo_lessons_card,
    val viewType: Int = 0,
    val isVisible: Boolean = false
):Cell()
data class BreakCell(
    var text: String?,
    var noEmpty: Boolean = true,
    val viewType: Int = 1,
    val viewSize: Int = 20,
):Cell()
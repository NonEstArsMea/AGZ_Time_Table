package com.NonEstArsMea.agz_time_table.domain.dataClass

import com.NonEstArsMea.agz_time_table.R

data class CellClass(
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

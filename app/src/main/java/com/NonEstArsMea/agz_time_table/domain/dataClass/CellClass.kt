package com.NonEstArsMea.agz_time_table.domain.dataClass

import com.NonEstArsMea.agz_time_table.R

data class CellClass(
    var subject: String,
    var teacher: String,
    var classroom: String,
    var studyGroup: String,
    var date: String,
    var subjectType: String,
    var startTime: String,
    var endTime: String,
    var subjectNumber: Int,
    var noEmpty: Boolean,
    var text: String,
    var lessonTheme: String,
    var color: Int = R.color.yellow_fo_lessons_card,
    val viewSize: Int,
    var isGone: Boolean = true,
    var department: String,
    var column: Int,
    var row: Int,
    var cellType: Int,
){
    companion object{
        const val LESSON_CELL_TYPE = 1
        const val BREAK_CELL_TYPE = 2
    }
}

package com.NonEstArsMea.agz_time_table.data.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.NonEstArsMea.agz_time_table.R

@Entity(tableName = "cell_classes")
data class CellClassDbModel(
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
    @PrimaryKey(autoGenerate = true)
    var id: Int
)
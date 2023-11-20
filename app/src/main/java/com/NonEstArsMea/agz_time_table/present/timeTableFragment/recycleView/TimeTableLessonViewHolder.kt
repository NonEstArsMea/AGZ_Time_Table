package com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.Methods
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

class TimeTableLessonViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val vhSubject = view.findViewById<TextView>(R.id.subject)
    val vhTeacher = view.findViewById<TextView>(R.id.teacher)
    val vhClassroom = view.findViewById<TextView>(R.id.classroom)
    val vhSubjectType = view.findViewById<TextView>(R.id.subject_type)
    val vhSeparatorColor = view.findViewById<View>(R.id.view_separator)
    val startTime = view.findViewById<TextView>(R.id.start_lesson_time)
    val endTime = view.findViewById<TextView>(R.id.end_lesson_time)
    val icon = view.findViewById<TextView>(R.id.lesson_number_text)

    fun bind(dayTimeTable: CellApi) {

        if (dayTimeTable.noEmpty == true) {
            vhSubject.text = dayTimeTable.subject
            vhTeacher.text = dayTimeTable.classroom
            vhClassroom.text = dayTimeTable.teacher
            vhSubjectType.text = dayTimeTable.subjectType
            dayTimeTable.color?.let { vhSeparatorColor.setBackgroundResource(it) }
            startTime.text = dayTimeTable.startTime
            endTime.text = dayTimeTable.endTime
            icon.text = dayTimeTable.subjectNumber.toString()
        }
    }
}
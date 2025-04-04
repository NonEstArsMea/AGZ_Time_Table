package com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.util.DateManager

class TimeTableLessonViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val vhSubject = view.findViewById<TextView>(R.id.subject)
    val vhTeacher = view.findViewById<TextView>(R.id.teacher)
    val vhClassroom = view.findViewById<TextView>(R.id.classroom)
    val vhSubjectType = view.findViewById<TextView>(R.id.subject_type)
    val vhSeparatorColor = view.findViewById<View>(R.id.view_separator)
    val startTime = view.findViewById<TextView>(R.id.start_lesson_time)
    val endTime = view.findViewById<TextView>(R.id.end_lesson_time)
    val icon = view.findViewById<TextView>(R.id.lesson_number_text)
    val add_info = view.findViewById<LinearLayout>(R.id.additional_information)
    val groupNumber = view.findViewById<TextView>(R.id.add_group)
    val dateNumber = view.findViewById<TextView>(R.id.add_date)
    val department = view.findViewById<TextView>(R.id.add_department)

    fun bind(dayTimeTable: CellClass, context: Context) {

        if (dayTimeTable.noEmpty) {
            vhSubject.text = dayTimeTable.subject
            vhTeacher.text = dayTimeTable.classroom
            vhClassroom.text = dayTimeTable.teacher
            vhSubjectType.text = dayTimeTable.lessonTheme + " " + dayTimeTable.subjectType
            vhSeparatorColor.setBackgroundResource(dayTimeTable.color)
            startTime.text = dayTimeTable.startTime
            endTime.text = dayTimeTable.endTime
            icon.text = dayTimeTable.subjectNumber.toString()
            groupNumber.text = context.getString(R.string.group_number) + "\n" + dayTimeTable.studyGroup
            dateNumber.text = DateManager.getDateString(context, dayTimeTable.date)
            add_info.visibility = View.GONE
            department.text = context.getString(R.string.department_number) + dayTimeTable.department
        }
    }
}
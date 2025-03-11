package com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.present.settingFragment.recycleView.CafClass

class DepartmentCardViewHolder(val view: View): ViewHolder(view) {
    private val separator = view.findViewById<View>(R.id.workload_view_separator)
    private val departmentName = view.findViewById<TextView>(R.id.workload_department_name)
    private val hoursText = view.findViewById<TextView>(R.id.workload_hours)
    fun bind(cafClass: CafClass) {
        departmentName.text = cafClass.department
        hoursText.text = cafClass.hours
        separator.setBackgroundColor(view.context.getColor(cafClass.getColor()))
    }
}
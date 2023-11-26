package com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

class BreakCellViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val text = view.findViewById<TextView>(R.id.text_break_cell)!!
    fun bind(breakCell: CellApi) {
        text.text = breakCell.text
        if (breakCell.viewSize != null) {
            text.setPadding(0, breakCell.viewSize, 0, breakCell.viewSize)
        }
    }

}
package com.NonEstArsMea.agz_time_table.present.viewholders

import android.view.View
import android.widget.TextView
import androidx.compose.ui.unit.dp
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.BreakCell

class BreakCellViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val text = view.findViewById<TextView>(R.id.text_break_cell)!!
    fun bind(breakCell: BreakCell) {
        text.text = breakCell.text
        text.setPadding(0, breakCell.viewSize, 0, breakCell.viewSize)
    }

}
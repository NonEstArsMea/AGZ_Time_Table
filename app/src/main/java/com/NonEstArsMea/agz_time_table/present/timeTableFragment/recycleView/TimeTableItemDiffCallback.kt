package com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView

import androidx.recyclerview.widget.DiffUtil
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass

class TimeTableItemDiffCallback : DiffUtil.ItemCallback<CellClass>() {
    override fun areItemsTheSame(oldItem: CellClass, newItem: CellClass): Boolean {
        return ((oldItem.text == newItem.text) and (oldItem.isGone == newItem.isGone))
    }

    override fun areContentsTheSame(oldItem: CellClass, newItem: CellClass): Boolean {
        return oldItem == newItem
    }
}
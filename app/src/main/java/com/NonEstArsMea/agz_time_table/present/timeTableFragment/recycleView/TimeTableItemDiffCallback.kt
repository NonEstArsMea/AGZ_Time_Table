package com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView

import androidx.recyclerview.widget.DiffUtil
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

class TimeTableItemDiffCallback : DiffUtil.ItemCallback<CellApi>() {
    override fun areItemsTheSame(oldItem: CellApi, newItem: CellApi): Boolean {
        return oldItem.text == newItem.text
    }

    override fun areContentsTheSame(oldItem: CellApi, newItem: CellApi): Boolean {
        return oldItem == newItem
    }
}
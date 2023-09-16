package com.NonEstArsMea.agz_time_table.present.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

class TimeTableDiffCallback: DiffUtil.ItemCallback<ArrayList<CellApi>>() {
    override fun areItemsTheSame(
        oldItem: ArrayList<CellApi>,
        newItem: ArrayList<CellApi>
    ): Boolean {
        return oldItem.size == newItem.size
    }

    override fun areContentsTheSame(
        oldItem: ArrayList<CellApi>,
        newItem: ArrayList<CellApi>
    ): Boolean {
        return oldItem == newItem
    }
}
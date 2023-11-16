package com.NonEstArsMea.agz_time_table.present.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.NonEstArsMea.agz_time_table.domain.dataClass.Cell
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

class TimeTableDiffCallback(
    val oldList: ArrayList<Cell>,
    val newList: List<Cell>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
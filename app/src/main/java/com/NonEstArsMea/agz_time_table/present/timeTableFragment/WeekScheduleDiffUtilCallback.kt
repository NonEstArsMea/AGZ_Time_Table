package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import androidx.recyclerview.widget.DiffUtil
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi


class WeekScheduleDiffUtilCallback(
    private val oldList: List<List<CellApi>>,
    private val newList: List<List<CellApi>>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
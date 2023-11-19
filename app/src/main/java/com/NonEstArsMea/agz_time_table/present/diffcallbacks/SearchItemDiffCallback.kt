package com.NonEstArsMea.agz_time_table.present.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

class SearchItemDiffCallback: DiffUtil.ItemCallback<MainParam>() {
    override fun areItemsTheSame(oldItem: MainParam, newItem: MainParam): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: MainParam,
        newItem: MainParam
    ): Boolean {
        return oldItem == newItem
    }
}
package com.NonEstArsMea.agz_time_table.present.diffcallbacks

import androidx.recyclerview.widget.DiffUtil

class SearchDiffCallback: DiffUtil.ItemCallback<ArrayList<String>>() {
    override fun areItemsTheSame(oldItem: ArrayList<String>, newItem: ArrayList<String>): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ArrayList<String>,
        newItem: ArrayList<String>
    ): Boolean {
        return oldItem == newItem
    }
}
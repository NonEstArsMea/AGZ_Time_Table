package com.NonEstArsMea.agz_time_table.present.searchFragment.recycleView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

class RVOnSearchFragmentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val name = view.findViewById<TextView>(R.id.rv_search_view)

    fun bind(param: MainParam) {
        name.text = param.name
    }
}
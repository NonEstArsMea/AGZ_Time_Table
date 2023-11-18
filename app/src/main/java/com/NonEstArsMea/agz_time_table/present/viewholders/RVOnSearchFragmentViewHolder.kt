package com.NonEstArsMea.agz_time_table.present.viewholders

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.google.android.material.transition.Hold

class RVOnSearchFragmentViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val name = view.findViewById<TextView>(R.id.rv_search_view)

    fun bind(param: MainParam){
        name.text = param.name
    }
}
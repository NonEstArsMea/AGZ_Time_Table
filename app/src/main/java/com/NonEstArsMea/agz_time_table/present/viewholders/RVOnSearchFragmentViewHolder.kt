package com.NonEstArsMea.agz_time_table.present.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R

class RVOnSearchFragmentViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val name = view.findViewById<TextView>(R.id.rv_search_view)
    val circle = view.findViewById<TextView>(R.id.red_circle)
}
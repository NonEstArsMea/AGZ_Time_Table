package com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.NonEstArsMea.agz_time_table.R

class NamingViewHolder(view: View) : ViewHolder(view) {

    val nameText = view.findViewById<TextView>(R.id.workload_naming_text)
    fun bind(text: String) {
        nameText.text = text
    }
}

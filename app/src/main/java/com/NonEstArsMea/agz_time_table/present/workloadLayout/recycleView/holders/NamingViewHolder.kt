package com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.util.DateManager

class NamingViewHolder(view: View) : ViewHolder(view) {

    val nameText = view.findViewById<TextView>(R.id.workload_naming_text)
    fun bind(text: String) {
        nameText.text = DateManager.getMonthNominativeСaseByString((text))
    }
}

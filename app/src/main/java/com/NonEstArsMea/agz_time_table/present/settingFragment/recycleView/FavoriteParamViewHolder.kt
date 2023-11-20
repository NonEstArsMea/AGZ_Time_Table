package com.NonEstArsMea.agz_time_table.present.settingFragment.recycleView

import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

class FavoriteParamViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val name = view.findViewById<TextView>(R.id.favorite_param_text)
    val delButton = view.findViewById<Button>(R.id.delete_button)
    val radBut = view.findViewById<RadioButton>(R.id.favorite_param_radiobutton)

    fun bind(mainParam: MainParam) {
        name.text = mainParam.name
    }

}
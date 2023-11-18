package com.NonEstArsMea.agz_time_table.present.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.present.diffcallbacks.SearchDiffCallback
import com.NonEstArsMea.agz_time_table.present.diffcallbacks.SearchListDiffCallback
import com.NonEstArsMea.agz_time_table.present.viewholders.FavoriteParamViewHolder
import com.NonEstArsMea.agz_time_table.present.viewholders.RVOnSearchFragmentViewHolder
import java.util.ArrayList

class SettingViewAdapter: ListAdapter<MainParam, FavoriteParamViewHolder>(SearchDiffCallback()) {



    var onClickListener:((MainParam)->(Unit))? = null
    var onDelClickListener:((MainParam)->(Unit))? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteParamViewHolder {

        val layout = when(viewType){
            DESABLET_TYPE -> R.layout.favorite_param_card_disabled
            ENEBLED_TYPE -> R.layout.favorite_param_card

            else -> throw java.lang.RuntimeException("ХЗ что за тип : $viewType ")
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)



        return FavoriteParamViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteParamViewHolder, position: Int) {
        val mainParam = getItem(position)

        holder.bind(mainParam)

        // Нажатие на объект
        holder.delButton.setOnClickListener {
            onDelClickListener?.invoke(mainParam)
        }

        holder.view.setOnClickListener {
            if (!holder.radBut.isChecked){
                onClickListener?.invoke(mainParam)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if(position == 0) ENEBLED_TYPE else DESABLET_TYPE
    }


    companion object{
        const val ENEBLED_TYPE = 1
        const val DESABLET_TYPE = 0
    }
}
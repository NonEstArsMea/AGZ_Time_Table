package com.NonEstArsMea.agz_time_table.present.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.present.viewholders.RVOnSearchFragmentViewHolder
import com.NonEstArsMea.agz_time_table.present.diffcallbacks.SearchDiffCallback

class RecycleViewOnSearchFragmentAdapter(
): ListAdapter<String, RVOnSearchFragmentViewHolder>(SearchDiffCallback()) {

    private var _listOfMainParam : ArrayList<MainParam> = ArrayList()

    var onMainParamClickListener:((String)->(Unit))? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVOnSearchFragmentViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.rv_on_search_view, parent, false)

        return RVOnSearchFragmentViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(listOfValues: MutableList<MainParam>){
        _listOfMainParam.clear()
        _listOfMainParam.addAll(listOfValues)

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RVOnSearchFragmentViewHolder, position: Int) {
        val mainParam = _listOfMainParam[position]
        holder.name.text = mainParam.name
        holder.circle.isVisible = mainParam.visible

        // Нажатие на объект
        holder.view.setOnClickListener {
            onMainParamClickListener?.invoke(mainParam.name)
        }
    }

    override fun getItemCount(): Int {
        return _listOfMainParam.size
    }
}
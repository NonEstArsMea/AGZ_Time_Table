package com.NonEstArsMea.agz_time_table.present.searchFragment.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

class RecycleViewOnSearchFragmentAdapter : ListAdapter<MainParam, RVOnSearchFragmentViewHolder>(
    SearchItemDiffCallback()
) {


    var onMainParamClickListener: ((MainParam) -> (Unit))? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVOnSearchFragmentViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.rv_on_search_view, parent, false)

        return RVOnSearchFragmentViewHolder(view)
    }


    override fun onBindViewHolder(holder: RVOnSearchFragmentViewHolder, position: Int) {
        val mainParam = getItem(position)
        holder.bind(getItem(position))
        holder.view.setOnClickListener {
            onMainParamClickListener?.invoke(mainParam)
        }
    }

}
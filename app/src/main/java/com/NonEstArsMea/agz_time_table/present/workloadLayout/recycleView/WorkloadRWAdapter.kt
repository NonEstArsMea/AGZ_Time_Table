package com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView.holders.DepartmentCardViewHolder
import com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView.holders.NamingViewHolder

class WorkloadRWAdapter(
    val onClick: (String, String, View, Int) -> Unit
) :
    ListAdapter<RWWorkloadClass, ViewHolder>(WorkloadItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            RWWorkloadClass.MONTH -> NamingViewHolder(
                inflater.inflate(R.layout.workload_naming_layout, parent, false)
            )

            else -> DepartmentCardViewHolder(inflater.inflate(R.layout.workload_department_card_layout, parent, false))
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder){
            is NamingViewHolder -> holder.bind((getItem(position) as MonthName).month)
            is DepartmentCardViewHolder -> {
                holder.bind(getItem(position) as CafClass, position)
                val cafClass = getItem(position) as CafClass
                val transitionName = "morph_$position"
                holder.view.transitionName = transitionName
                holder.view.setOnClickListener {
                    onClick(cafClass.month, cafClass.depNumber, it, position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is MonthName) {
            RWWorkloadClass.MONTH
        } else {
            RWWorkloadClass.DEPARTMENT_CARD
        }
    }
}

class WorkloadItemDiffCallback : DiffUtil.ItemCallback<RWWorkloadClass>() {
    override fun areItemsTheSame(oldItem: RWWorkloadClass, newItem: RWWorkloadClass): Boolean {
        return ((oldItem == newItem))
    }

    override fun areContentsTheSame(oldItem: RWWorkloadClass, newItem: RWWorkloadClass): Boolean {
        return oldItem == newItem
    }
}
package com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.NonEstArsMea.agz_time_table.present.settingFragment.recycleView.CafClass
import com.NonEstArsMea.agz_time_table.present.settingFragment.recycleView.Naming
import com.NonEstArsMea.agz_time_table.present.settingFragment.recycleView.RWWorkloadClass

class WorkloadRWAdapter :
    ListAdapter<RWWorkloadClass, DepartmentCardViewHolder>(WorkloadItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentCardViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: DepartmentCardViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is Naming) {
            RWWorkloadClass.NAMING
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
package com.NonEstArsMea.agz_time_table.present.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellMapper
import com.NonEstArsMea.agz_time_table.present.diffcallbacks.TimeTableItemDiffCallback
import com.NonEstArsMea.agz_time_table.present.viewholders.BreakCellViewHolder
import com.NonEstArsMea.agz_time_table.present.viewholders.TimeTableLessonViewHolder

class TimeTableRecycleViewAdapter: ListAdapter<CellApi, RecyclerView.ViewHolder>(
    TimeTableItemDiffCallback()
) {

    val mapper = CellMapper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            LESSON_TIME_TABLE_TYPE -> TimeTableLessonViewHolder(inflater.inflate(R.layout.one_lesson_card, parent, false))
            else -> BreakCellViewHolder(inflater.inflate(R.layout.break_cell_layout, parent, false))
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is TimeTableLessonViewHolder -> holder.bind( getItem(position))
            is BreakCellViewHolder -> holder.bind( getItem(position))
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position).text == null) {
            LESSON_TIME_TABLE_TYPE
        }else{
            BREAK_CELL_TYPE
        }
    }

    companion object{
        const val LESSON_TIME_TABLE_TYPE = 0
        const val BREAK_CELL_TYPE = 1
    }

}

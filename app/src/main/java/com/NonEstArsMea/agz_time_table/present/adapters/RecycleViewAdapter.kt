package com.NonEstArsMea.agz_time_table.present.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.BreakCell
import com.NonEstArsMea.agz_time_table.domain.dataClass.Cell
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.LessonTimeTable
import com.NonEstArsMea.agz_time_table.present.diffcallbacks.TimeTableDiffCallback
import com.NonEstArsMea.agz_time_table.present.viewholders.BreakCellViewHolder
import com.NonEstArsMea.agz_time_table.present.viewholders.TimeTableLessonViewHolder

class RecycleViewAdapter: ListAdapter<ArrayList<CellApi>, RecyclerView.ViewHolder>(TimeTableDiffCallback()) {

    var timeTableDayList : ArrayList<Cell> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newTimeTableDayList: List<Cell>){
        timeTableDayList.clear()
        timeTableDayList.addAll(newTimeTableDayList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            0 -> TimeTableLessonViewHolder(inflater.inflate(R.layout.one_lesson_card, parent, false))
            else -> BreakCellViewHolder(inflater.inflate(R.layout.break_cell_layout, parent, false))
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is TimeTableLessonViewHolder -> holder.bind( timeTableDayList[position] as LessonTimeTable)
            is BreakCellViewHolder -> holder.bind( timeTableDayList[position] as BreakCell)
        }


    }

    override fun getItemViewType(position: Int): Int {
        return when(timeTableDayList[position]){
            is LessonTimeTable -> 0
            is BreakCell -> 1
        }
    }

    override fun getItemCount(): Int {
        return timeTableDayList.size
    }



}

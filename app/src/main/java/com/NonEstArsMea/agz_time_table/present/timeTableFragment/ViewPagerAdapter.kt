package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView.RecycleViewFragment

class ViewPagerAdapter(
    fm: Fragment,
) : FragmentStateAdapter(fm) {

    private val weekSchedule: ArrayList<List<CellApi>> = ArrayList()

    fun setData(newWeekSchedule: List<List<CellApi>>) {
        val diffResult =
            DiffUtil.calculateDiff(WeekScheduleDiffUtilCallback(weekSchedule, newWeekSchedule))
        weekSchedule.clear()
        Log.e("tag2", "$newWeekSchedule")
        weekSchedule.addAll(newWeekSchedule)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun getItemCount() = weekSchedule.size

    override fun createFragment(dayOfWeek: Int): Fragment {
        return RecycleViewFragment.newInstance(weekSchedule[dayOfWeek])
    }

}
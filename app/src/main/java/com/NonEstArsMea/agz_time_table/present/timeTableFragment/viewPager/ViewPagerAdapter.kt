package com.NonEstArsMea.agz_time_table.present.timeTableFragment.viewPager

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView.RecycleViewFragment

class ViewPagerAdapter(
    fm: Fragment,
) : FragmentStateAdapter(fm) {

    private val weekSchedule: ArrayList<List<CellClass>> = ArrayList()

    fun setData(newWeekSchedule: List<List<CellClass>>) {
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
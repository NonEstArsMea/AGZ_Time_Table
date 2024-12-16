package com.NonEstArsMea.agz_time_table.present.timeTableFragment.viewPager

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView.RecycleViewFragment

class ViewPagerAdapter(
    fm: Fragment,
) : FragmentStateAdapter(fm) {

    private val weekSchedule: MutableList<List<CellClass>> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newWeekSchedule: List<List<CellClass>>) {
        if (weekSchedule != newWeekSchedule) { // Проверка на изменения
            weekSchedule.clear()
            weekSchedule.addAll(newWeekSchedule)
            notifyDataSetChanged() // Уведомляем об изменении данных
        }
    }


    override fun getItemCount() = weekSchedule.size

    override fun createFragment(dayOfWeek: Int): Fragment {
        return RecycleViewFragment.newInstance(weekSchedule[dayOfWeek])
    }

    override fun getItemId(position: Int): Long {
        return weekSchedule[position].hashCode().toLong() // Уникальный идентификатор для каждой страницы
    }

    override fun containsItem(itemId: Long): Boolean {
        return weekSchedule.any { it.hashCode().toLong() == itemId }
    }

}
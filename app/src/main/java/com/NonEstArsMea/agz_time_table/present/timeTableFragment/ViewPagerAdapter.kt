package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView.RecycleViewFragment

class ViewPagerAdapter(
    fm: Fragment,
):FragmentStateAdapter(fm) {

    // массив расписания на неделю
    val ttw : ArrayList<ArrayList<CellApi>> = ArrayList()
    var arl: ArrayList<CellApi> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newTTW: ArrayList<ArrayList<CellApi>>){
        ttw.clear()
        ttw.addAll(newTTW)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData(){
        ttw.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() = 6

    override fun createFragment(dayOfWeek: Int): Fragment {
        for(a in 1..6){
            ttw.add(arl)
        }
        return RecycleViewFragment.newInstance(ttw[dayOfWeek])
    }

}
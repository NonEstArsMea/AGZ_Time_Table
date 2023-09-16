package com.NonEstArsMea.agz_time_table.present.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellMapper
import com.NonEstArsMea.agz_time_table.present.adapters.RecycleViewAdapter

class RecycleViewFragment(
    timeTableDay: ArrayList<CellApi>,
): Fragment() {

    var _timeTableDay = timeTableDay
    val adapter = RecycleViewAdapter()

    private lateinit var lm: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.recycle_view_view_pager, container)
        val rvTimeTable = view.findViewById<RecyclerView>(R.id.view_pager_recycler)

        rvTimeTable.layoutManager = LinearLayoutManager(context)

        if(!_timeTableDay.isEmpty()) {
            adapter.setData(_timeTableDay.map{
                CellMapper().cellApiToUI(api = it)
            })
            rvTimeTable.adapter = adapter
        }
        return view
    }


    companion object{
        fun newInstance(timeTableDay: ArrayList<CellApi>):RecycleViewFragment{
            return RecycleViewFragment(timeTableDay)
        }
    }
}
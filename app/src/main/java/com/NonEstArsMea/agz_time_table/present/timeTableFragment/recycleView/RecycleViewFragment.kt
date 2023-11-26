package com.NonEstArsMea.agz_time_table.present.timeTableFragment.recycleView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RecycleViewFragment : Fragment() {

    private val adapter = TimeTableRecycleViewAdapter()
    lateinit var timeTableDay: List<CellApi>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recycle_view_view_pager, container)
        val rvTimeTable = view.findViewById<RecyclerView>(R.id.view_pager_recycler)
        rvTimeTable.adapter = adapter
        arguments?.let {
            val type: Type = object : TypeToken<List<CellApi>>() {}.type
            timeTableDay = Gson().fromJson(it.getString(ARGUMENTS), type)
            rvTimeTable.layoutManager = LinearLayoutManager(context)
            adapter.submitList(timeTableDay)
        }

//        adapter.onHolderClickListener = {
//            timeTableDay[it].isGone = !timeTableDay[it].isGone
//            adapter.submitList(timeTableDay)
//            rvTimeTable.adapter = adapter
//        }

        return view
    }


    companion object {

        const val ARGUMENTS = "ttw"

        fun newInstance(ttw: List<CellApi>): RecycleViewFragment {
            return RecycleViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENTS, Gson().toJson(ttw))
                }
            }
        }
    }
}



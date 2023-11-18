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
import com.NonEstArsMea.agz_time_table.present.adapters.RecycleViewAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RecycleViewFragment: Fragment() {

    val adapter = RecycleViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.recycle_view_view_pager, container)
        val rvTimeTable = view.findViewById<RecyclerView>(R.id.view_pager_recycler)
        val args = this.arguments
        rvTimeTable.adapter = adapter
        args.let {
            val type: Type = object : TypeToken<ArrayList<CellApi>>() {}.type
            val timeTableDay = Gson().fromJson<ArrayList<CellApi>>(args?.getString(ARGUMENTS), type)
            rvTimeTable.layoutManager = LinearLayoutManager(context)
            if (timeTableDay.isNotEmpty()) {
                adapter.submitList(timeTableDay)
            }
        }

        return view
    }


    companion object{

        const val ARGUMENTS = "ttw"

        fun newInstance(ttw: ArrayList<CellApi>):RecycleViewFragment{
            return RecycleViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENTS, Gson().toJson(ttw))
                }
            }
        }
    }
}



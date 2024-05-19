package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.TableLayoutBinding


class TableFragment : Fragment() {

    private var _binding:TableLayoutBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {


        _binding = TableLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        @JvmStatic fun newInstance() = TableFragment()
    }
}
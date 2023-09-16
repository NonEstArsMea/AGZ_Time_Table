package com.NonEstArsMea.agz_time_table.present.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.SettingLayoutBinding

class SettingFragment: Fragment() {
    private var _binding: SettingLayoutBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingLayoutBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.searchButton.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //Log.e("my_tag","Search destroy")
    }
}
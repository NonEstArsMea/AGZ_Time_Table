package com.NonEstArsMea.agz_time_table.present.departmentalTimetableFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.NonEstArsMea.agz_time_table.databinding.DepartmentalTimeTableLayoutBinding

class DepartmentalTimeTableFragment : Fragment() {

    private var _binding: DepartmentalTimeTableLayoutBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DepartmentalTimeTableLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.exitButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
package com.NonEstArsMea.agz_time_table.present.cafTimeTable

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.NonEstArsMea.agz_time_table.databinding.AudWorkloadLayoutBinding
import com.NonEstArsMea.agz_time_table.databinding.CafTimeTableLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.audWorkloadFragment.AudWorkloadViewModel
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import javax.inject.Inject

class CafTimeTableFragment: Fragment() {

    private var _binding: CafTimeTableLayoutBinding? = null
    private val binding get() = _binding!!

    lateinit var vm: CafTimeTableViewModel

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        vm = ViewModelProvider(this, viewModelFactory)[CafTimeTableViewModel::class.java]

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = CafTimeTableLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        observeVM()

    }



    override fun onResume() {
        super.onResume()


        binding.btnOk.setOnClickListener {
            if(binding.inputField.text.isNotEmpty()){
                vm.getData(binding.inputField.text.toString())
            }
        }

        binding.btnLeft.setOnClickListener {
            vm.setNewDate(-1)
        }

        binding.btnRight.setOnClickListener {
            vm.setNewDate(1)
        }

    }

    private fun observeVM() {
        vm.state.observe(viewLifecycleOwner){
            when(it){
                ConnectionError -> TODO()
                is SetDate -> {
                    binding.dateText.text = it.date
                }
                is DataIsLoad -> {
                    binding.dateText.text = it.date
                    Log.e("resume", "draw table")
                    binding.audWorkloadTableView.setCafTimeTable(it.rep, it.unicList)
                }
            }
        }
    }



}
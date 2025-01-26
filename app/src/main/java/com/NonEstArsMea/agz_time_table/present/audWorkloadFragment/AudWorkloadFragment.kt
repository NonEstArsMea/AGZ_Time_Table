package com.NonEstArsMea.agz_time_table.present.audWorkloadFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.AudWorkloadLayoutBinding
import com.NonEstArsMea.agz_time_table.databinding.LoginLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.loginLayout.LoginViewModel
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import javax.inject.Inject

class AudWorkloadFragment : Fragment() {


    private var _binding: AudWorkloadLayoutBinding? = null
    private val binding get() = _binding!!

    lateinit var vm: AudWorkloadViewModel

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    private val educationalBuildings: List<TextView> by lazy {
        listOf(
            binding.educationalBuilding1,
            binding.educationalBuilding2,
            binding.educationalBuilding3,
            binding.educationalBuilding4,
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        vm = ViewModelProvider(this, viewModelFactory)[AudWorkloadViewModel::class.java]

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

        _binding = AudWorkloadLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        Log.e("resume", "resume")
        observeVM()

        educationalBuildings.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                Log.e("resume", "position" + index.toString())
                vm.setPosition(index)
            }
        }

        vm.position.observe(viewLifecycleOwner) { position ->
            updateStyles(position)
            vm.getNewBuilding(position)

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
                    binding.audWorkloadTableView.setDateTable(it.list, it.unicList)
                }
            }
        }
    }

    private fun updateStyles(position: Int) {
        educationalBuildings.forEachIndexed { index, textView ->
            if (index == position) {
                textView.setTextAppearance(R.style.MainTextViewStyle_DayNowWeekNumber)
                textView.background = ContextCompat.getDrawable(requireContext(), R.drawable.break_cell_background)
            } else {
                textView.setTextAppearance(R.style.MainTextViewStyle_WeekNumber)
                textView.background = ContextCompat.getDrawable(requireContext(), R.drawable.main_surface)
            }
        }
    }

}
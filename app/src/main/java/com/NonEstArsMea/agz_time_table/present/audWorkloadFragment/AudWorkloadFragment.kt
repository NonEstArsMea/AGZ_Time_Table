package com.NonEstArsMea.agz_time_table.present.audWorkloadFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.AudWorkloadLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.google.android.material.transition.MaterialContainerTransform
import javax.inject.Inject

class AudWorkloadFragment : Fragment() {


    private var _binding: AudWorkloadLayoutBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    val vm: AudWorkloadViewModel by viewModels { viewModelFactory }

    private val component by lazy {
        (requireContext().applicationContext as TimeTableApplication).component
    }

    private var educationalBuildings: List<TextView> = listOf()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        //vm = ViewModelProvider(this, viewModelFactory)[AudWorkloadViewModel::class.java]

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainerView
            duration = 1500
            scrimColor = android.graphics.Color.TRANSPARENT
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AudWorkloadLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateList()
        observeVM()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onResume() {
        super.onResume()
        updateStyles(0)
        listOf(
            binding.educationalBuilding1,
            binding.educationalBuilding2,
            binding.educationalBuilding3,
            binding.educationalBuilding4,
        ).forEachIndexed { index, textView ->
            textView.setOnClickListener {
                vm.setPosition(index)
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
                    binding.dateText.text = DateManager.getDateString(requireContext(), it.date)
                }
                is DataIsLoad -> {
                    updateStyles(it.position)
                    binding.dateText.text = DateManager.getDateString(requireContext(), it.date)
                    binding.audWorkloadTableView.setDateTable(it.list, it.unicList)
                }
            }
        }
    }

    private fun updateStyles(position: Int) {
        educationalBuildings.forEachIndexed { index, textView ->
            if (index == position) {
                TextViewCompat.setTextAppearance(textView, R.style.MainTextViewStyle_DayNowWeekNumber)
                textView.background = ContextCompat.getDrawable(requireContext(), R.drawable.break_cell_background)
            } else {
                TextViewCompat.setTextAppearance(textView, R.style.MainTextViewStyle_WeekNumber)
                textView.background = ContextCompat.getDrawable(requireContext(), R.drawable.main_surface)
            }
        }
    }

    private fun updateList(){
        educationalBuildings = listOf(
            binding.educationalBuilding1,
            binding.educationalBuilding2,
            binding.educationalBuilding3,
            binding.educationalBuilding4,
        )
    }
}
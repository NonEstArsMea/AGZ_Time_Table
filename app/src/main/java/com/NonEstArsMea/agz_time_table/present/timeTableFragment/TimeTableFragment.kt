package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.TimeTableFragmentBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.util.animateSlideText
import javax.inject.Inject


class TimeTableFragment : Fragment() {

    private lateinit var vm: TimeTableViewModel

    private var _binding: TimeTableFragmentBinding? = null
    private val binding get() = _binding!!

    private var viewModeIsPager = true


    @Inject
    lateinit var timeTableViewModelFactory: MainViewModelFactory

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        vm = ViewModelProvider(
            this,
            timeTableViewModelFactory
        )[TimeTableViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TimeTableFragmentBinding.inflate(inflater, container, false)


        binding.mainParam.setOnClickListener {
            if(vm.ifNotFirstBeginning()){
                vm.getNextMainParam()
            }else{
                findNavController().navigate(R.id.searchFragment)
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModeIsPager = true
        updateViewMode()

        binding.setDateButton.setOnClickListener {
            vm.getNewTimeTable(0)
        }

        binding.changeViewMod.setOnClickListener {
            updateViewMode()
        }

        vm.checkMainParam()


        vm.month.observe(viewLifecycleOwner){
            binding.monthDate.text = it
        }

        observeViewModel()
    }

    private fun updateViewMode() {
        if (viewModeIsPager) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.view_pager_and_table_container, ViewPagerFragment.newInstance())
                .commit()
        } else {
            parentFragmentManager.beginTransaction()
                .replace(R.id.view_pager_and_table_container, TableFragment.newInstance()).commit()
        }
        viewModeIsPager = !viewModeIsPager

    }

    private fun observeViewModel() {
        vm.mainParam.observe(viewLifecycleOwner) {
            binding.mainParam.animateSlideText(it.name)
        }

    }

    override fun onResume() {
        super.onResume()
        vm.startFragment()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
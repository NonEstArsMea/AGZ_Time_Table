package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.TableLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.viewPager.ViewPagerAdapter
import javax.inject.Inject


class TableFragment : Fragment() {

    private var _binding:TableLayoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: TimeTableViewModel

    @Inject
    lateinit var timeTableViewModelFactory: MainViewModelFactory

    private val component by lazy {
        (requireActivity().application as TimeTableApplication).component
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        vm = ViewModelProvider(
            requireParentFragment(),
            timeTableViewModelFactory
        )[TimeTableViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {


        _binding = TableLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        observeViewModel()

        binding.weekDateText.text = vm.getWeekDateText()

        binding.buttonLeft.setOnClickListener {
            updateData(PREVIOUS_WEEK)
        }

        binding.buttonRight.setOnClickListener {
            updateData(NEXT_WEEK)
        }
    }

    private fun observeViewModel() {
        vm.state.observe(viewLifecycleOwner) {
                when (it) {
                    is LoadData -> {
                        val list = vm.timeTableFromStorage()
                        val dateList = vm.getDateList()
                        binding.tabView.setTimeTable(list, dateList)
                    }

                    is ConnectionError -> {}

                    is TimeTableIsLoad -> {
                        Log.e("tag", it.toString())
                        val dateList = vm.getDateList()
                        binding.tabView.setTimeTable(it.list, dateList)
                        binding.weekDateText.text = vm.getWeekDateText()
                    }
                }
        }
    }

    private fun updateData(newTime: Int? = null) {
        vm.getNewTimeTable(newTime)
    }

    companion object {
        private const val PREVIOUS_WEEK = -7
        private const val NEXT_WEEK = 7

        @JvmStatic fun newInstance() = TableFragment()
    }
}
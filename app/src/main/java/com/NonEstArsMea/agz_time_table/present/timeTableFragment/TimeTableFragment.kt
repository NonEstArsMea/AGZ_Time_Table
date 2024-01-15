package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.NonEstArsMea.agz_time_table.databinding.TimeTableFragmentBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.viewPager.ViewPagerAdapter
import javax.inject.Inject


class TimeTableFragment : Fragment() {

    private lateinit var vm: TimeTableViewModel

    private var _binding: TimeTableFragmentBinding? = null
    private val binding get() = _binding!!

    var days = mutableListOf<TextView>()
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    private lateinit var viewPager: ViewPager2


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

        days = listOf(
            binding.day1,
            binding.day2,
            binding.day3,
            binding.day4,
            binding.day5,
            binding.day6,
        ).toMutableList()



        binding.mainParam.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewPager = binding.viewPagerTimeTableFragment
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                context?.let { context ->
                    days.toList().forEach {
                        it.setTextAppearance(R.style.MainTextViewStyle_WeekNumber)
                        it.background =
                            resources.getDrawable(R.drawable.main_surface, context.theme)
                    }
                    with(days[position]) {
                        this.setTextAppearance(R.style.MainTextViewStyle_DayNowWeekNumber)
                        this.background =
                            resources.getDrawable(R.drawable.break_cell_background, context.theme)
                    }
                }

            }
        })


        // Слушатель на дни
        days.toList().forEachIndexed { index, textView ->
            textView.setOnClickListener {
                viewPager.setCurrentItem(index, true)
            }
        }

        binding.buttomLeft.setOnClickListener {
            updateData(PREVIOUS_WEEK)
        }

        binding.buttomRight.setOnClickListener {
            updateData(NEXT_WEEK)
        }

        binding.setDateButton.setOnClickListener {
            updateData(NOW_WEEK)
        }

        vm.checkMainParam()

        binding.monthDate.text = vm.getMonth()
        setButtonNumbers()

        observeViewModel()


    }


    override fun onStart() {
        super.onStart()
        vm.startFragment()
    }

    private fun observeViewModel() {

        vm.lll.observe(viewLifecycleOwner){
            Log.e("storrage_5", it.toString())
        }

        vm.mainParam.observe(viewLifecycleOwner) {
            binding.mainParam.text = it.name
        }

        vm.state.observe(viewLifecycleOwner) {
            viewPager.doOnLayout { _ ->
                when (it) {
                    is LoadData -> {
                        binding.progressBar.isVisible = true
                        val list = vm.timeTableFromStorage() ?: emptyList()
                        Log.e("storrage_2", list.toString())
                        viewPagerAdapter.setData(list)
                    }

                    is ConnectionError -> {
                        binding.progressBar.isVisible = false
                    }

                    is TimeTableIsLoad -> {
                        binding.progressBar.isVisible = false
                        viewPagerAdapter.setData(it.list)
                        viewPager.post {
                            viewPager.currentItem = vm.getCurrentItem()
                        }
                    }
                }
            }


        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setButtonNumbers() {
        DateManager.dayNumberOnButton().forEachIndexed { index, s ->
            days[index].text = s
        }
    }

    private fun updateData(newTime: Int? = null) {
        vm.getNewTimeTable(newTime)
        binding.monthDate.text = vm.getMonth()
        setButtonNumbers()
    }

    companion object {
        private const val PREVIOUS_WEEK = -7
        private const val NEXT_WEEK = 7
        private const val NOW_WEEK = 0
    }

}
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.ViewPagerBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModelFactory
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.viewPager.ViewPagerAdapter
import com.NonEstArsMea.agz_time_table.util.DateManager
import javax.inject.Inject


class ViewPagerFragment : Fragment() {


    private lateinit var vm: TimeTableViewModel

    private var days = mutableListOf<TextView>()


    private var _binding: ViewPagerBinding? = null
    private val binding get() = _binding!!

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewPagerBinding.inflate(layoutInflater)

        days = listOf(
            binding.day1,
            binding.day2,
            binding.day3,
            binding.day4,
            binding.day5,
            binding.day6,
        ).toMutableList()


        binding.buttomLeft.setOnClickListener {
            updateData(PREVIOUS_WEEK)
        }

        binding.buttomRight.setOnClickListener {
            updateData(NEXT_WEEK)
        }

        val viewPager: ViewPager2 = binding.viewPagerTimeTableFragment
        val viewPagerAdapter = ViewPagerAdapter(this)

        // Слушатель на дни
        days.toList().forEachIndexed { index, textView ->
            textView.setOnClickListener {
                viewPager.setCurrentItem(index, true)
            }
        }

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

        updateData(NOW_WEEK)
        setButtonNumbers()
        observeViewModel(viewPager, viewPagerAdapter)

        return binding.root
    }

    private fun observeViewModel(viewPager: ViewPager2, viewPagerAdapter: ViewPagerAdapter) {
        vm.state.observe(viewLifecycleOwner) {
            viewPager.doOnLayout { _ ->
                when (it) {
                    is LoadData -> {
                        val list = vm.timeTableFromStorage()
                        viewPagerAdapter.setData(list)
                    }

                    is ConnectionError -> {}

                    is TimeTableIsLoad -> {
                        viewPagerAdapter.setData(it.list)
                        viewPager.post {
                            viewPager.currentItem = vm.getCurrentItem()
                        }
                        setButtonNumbers()
                    }
                }
            }
        }
    }


    private fun setButtonNumbers() {
        DateManager.dayNumberOnButton().forEachIndexed { index, s ->
            days[index].text = s
        }
    }

    private fun updateData(newTime: Int? = null) {
        vm.getNewTimeTable(newTime)
    }

    companion object {

        private const val PREVIOUS_WEEK = -7
        private const val NEXT_WEEK = 7
        private const val NOW_WEEK = 0

        @JvmStatic
        fun newInstance() = ViewPagerFragment()

    }
}
package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.databinding.TimeTableFragmentBinding


class TimeTableFragment : Fragment() {

    private lateinit var vm: TimeTableViewModel

    private var _binding: TimeTableFragmentBinding? = null
    private val binding get() = _binding!!

    var days = mutableListOf<TextView>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        vm = ViewModelProvider(
            this,
            TimeTableViewModelFactory(context)
        )[TimeTableViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // биндинг фрагмента
        _binding = TimeTableFragmentBinding.inflate(inflater, container, false)


        // массив с вьюшками дней
        days = listOf(
            binding.day1,
            binding.day2,
            binding.day3,
            binding.day4,
            binding.day5,
            binding.day6,
        ).toMutableList()



        vm.dataLiveData.observe(viewLifecycleOwner) {
            vm.getTimeTable()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Создание пэйджера
        val viewPager = binding.viewPagerTimeTableFragment
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                days.toList().forEach {
                    it.setTextAppearance(R.style.MainTextViewStyle_WeekNumber)
                    it.background = resources.getDrawable(R.drawable.main_surface, context!!.theme)
                }
                with(days[position]) {
                    this.setTextAppearance(R.style.MainTextViewStyle_DayNowWeekNumber)
                    this.background =
                        resources.getDrawable(R.drawable.break_cell_background, context!!.theme)
                }
            }
        })


        // Слушатель на дни
        days.toList().forEachIndexed { index, textView ->
            textView.setOnClickListener {
                viewPager.setCurrentItem(index, true)
            }
        }

        // Слушатель на left_buttom
        binding.buttomLeft.setOnClickListener {
            updateData(PREVIOUS_WEEK)
        }

        binding.buttomRight.setOnClickListener {
            updateData(NEXT_WEEK)
        }


        //обновление данных


        // Слушатель на расписание недели (при перелистывании недели)
        vm.timeTableChanged.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewPagerAdapter.setData(it)
                viewPager.adapter = viewPagerAdapter
                binding.viewPagerTimeTableFragment.currentItem = DateRepositoryImpl.getDayOfWeek()
            }
        }

        vm.loading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        vm.mainParam.observe(viewLifecycleOwner) {
            binding.mainParam.text = it.name
        }

        updateData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Установка дат дней недели
    private fun setButtonNumbers() {
        DateRepositoryImpl.dayNumberOnButton().forEachIndexed { index, s ->
            days[index].text = s
        }
    }

    // Обновление данных
    private fun updateData(newTime: Int? = null) {
        vm.getTimeTable(newTime)
        binding.monthDate.text = DateRepositoryImpl.monthAndDayNow()
        binding.viewPagerTimeTableFragment.currentItem = DateRepositoryImpl.getDayOfWeek()
        setButtonNumbers()

    }

    companion object {
        private const val PREVIOUS_WEEK = -7
        private const val NEXT_WEEK = 7
    }

}
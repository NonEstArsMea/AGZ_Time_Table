package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.lazy.LazyRow
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.databinding.TimeTableFragmentBinding
import com.NonEstArsMea.agz_time_table.present.customDateView.CastomDateFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar


class TimeTableFragment : Fragment() {

    private lateinit var vm: TimeTableViewModel

    private var _binding: TimeTableFragmentBinding? = null
    private val binding get() = _binding!!

    var days = mutableListOf<TextView>()
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    // Установка пикера
    private val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

    private lateinit var viewPager: ViewPager2



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

        Log.e("fragment", "create")


        // Создание пэйджера
        viewPager = binding.viewPagerTimeTableFragment
        viewPagerAdapter = ViewPagerAdapter(this)
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



        // Слушатель на расписание недели (при перелистывании недели)
        vm.timeTableChanged.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewPager.adapter = viewPagerAdapter
                viewPagerAdapter.setData(it)
                binding.viewPagerTimeTableFragment.currentItem = DateRepositoryImpl.getDayOfWeek()
            }

        }

        vm.loading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        vm.mainParam.observe(viewLifecycleOwner) {
            if(binding.mainParam.text != it.name){
                binding.mainParam.text = it.name
            }
            binding.viewPagerTimeTableFragment.setCurrentItem(DateRepositoryImpl.getDayOfWeek(),true)
        }


        binding.setDateButton.setOnClickListener {
            datePicker.show(requireActivity().supportFragmentManager, datePicker.toString())
        }
        /**
         *  Нажатие на пикер
         *  datePicker.clearOnPositiveButtonClickListeners() - исправляет интерестный баг
         *  Слушатель с предыдущего раза не выключается, и фрагменты начинают создаваться
         *  по несколько штук
         */
        datePicker.addOnPositiveButtonClickListener {
            val calender = Calendar.getInstance()
            calender.timeInMillis = it
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val month = calender.get(Calendar.MONTH)
            val year = calender.get(Calendar.YEAR)
            val mainParam = vm.getMainParam()
            Log.e("picker", "$day $month $year $mainParam ")
            try {
                findNavController().navigate(TimeTableFragmentDirections
                    .actionTimeTableFragmentToCastomDateFragment(
                        day = day,
                        month = month,
                        year = year,
                        mainParam = mainParam))
                datePicker.clearOnPositiveButtonClickListeners()
            }catch (_: Exception){

            }



        }
        binding.monthDate.text = DateRepositoryImpl.monthAndDayNow()
    }

    override fun onResume() {
        super.onResume()
        viewPager.setCurrentItem(0)
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
        binding.viewPagerTimeTableFragment.currentItem = DateRepositoryImpl.getDayOfWeek()
        vm.getNewTimeTable(newTime)
        binding.monthDate.text = DateRepositoryImpl.monthAndDayNow()
        setButtonNumbers()
    }

    companion object {
        private const val PREVIOUS_WEEK = -7
        private const val NEXT_WEEK = 7
    }

}
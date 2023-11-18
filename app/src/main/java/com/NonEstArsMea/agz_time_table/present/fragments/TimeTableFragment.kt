package com.NonEstArsMea.agz_time_table.present.fragments

import android.annotation.SuppressLint
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.ThemeUtils
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.TimeTableFragmentBinding
import com.NonEstArsMea.agz_time_table.domain.GetDateRepositoryImpl
import com.NonEstArsMea.agz_time_table.present.MainViewModel
import com.NonEstArsMea.agz_time_table.present.adapters.ViewPagerAdapter
import java.util.Calendar


class TimeTableFragment : Fragment() {
    // сохраниение даты в этой переменной
    lateinit var calendar: Calendar

    private val vm: MainViewModel by activityViewModels()

    private var _binding: TimeTableFragmentBinding? = null
    private val binding get() = _binding!!

    var days = mutableListOf<TextView>()

    var currentItem: Int = 0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // биндинг фрагмента
        _binding = TimeTableFragmentBinding.inflate(inflater, container, false)

        // Календарь
        calendar = vm.getCalendar()


        // массив с вьюшками дней
        days = listOf(binding.day1,
            binding.day2,
            binding.day3,
            binding.day4,
            binding.day5,
            binding.day6,).toMutableList()

        // Создание пэйджера
        val viewPager = binding.viewPagerTimeTableFragment
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                days.toList().forEach{
                    it.setTextAppearance(R.style.MainTextViewStyle_WeekNumber)
                    it.background = resources.getDrawable(R.drawable.main_surface, context!!.theme)
                }
                with(days[position]){
                    this.setTextAppearance(R.style.MainTextViewStyle_DayNowWeekNumber)
                    this.background = resources.getDrawable(R.drawable.break_cell_background, context!!.theme)
                }
            }
        })



        // Слушатель на дни
        days.toList().forEachIndexed { index, textView ->
            textView.setOnClickListener {
                Log.e("curr 4", index.toString())
                viewPager.setCurrentItem(index, true)
            }
        }

        // Слушатель на left_buttom
        binding.buttomLeft.setOnClickListener {
            currentItem = 0
            updateData(PREVIOUS_WEEK)
        }

        binding.buttomRight.setOnClickListener {
            currentItem = 0
            updateData(NEXT_WEEK)
        }

        //обновление mainParam
        binding.mainParam.text = vm.getMainParam()

        currentItem = GetDateRepositoryImpl(calendar).getDayOfWeek()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("my--tag", "onViewCreated")

        val viewPagerAdapter = ViewPagerAdapter(this)
        // Слушатель на состояние загрузки
        vm.loading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = (it == true)
            if(it == true){
                viewPagerAdapter.clearData()
                binding.viewPagerTimeTableFragment.adapter = viewPagerAdapter
            }
        }
        //обновление данных
        updateData()
        // Массив из дней в строковой форме для парсинга
        val days = GetDateRepositoryImpl(calendar).getArrayOfWeekDate()
        vm.dataLiveData.observe(viewLifecycleOwner){ data ->
            vm.getTimeTable(data, days, vm.getMainParam())
        }

        // Слушатель на расписание недели (при перелистывании недели)
        vm.weekTimeTable.observe(viewLifecycleOwner){
            if(!it.isEmpty()){
                viewPagerAdapter.setData(it)
                binding.viewPagerTimeTableFragment.adapter = viewPagerAdapter
                binding.viewPagerTimeTableFragment.currentItem = currentItem
            }
        }

        // Слушатель на дату
        vm.calendarLiveData.observe(viewLifecycleOwner){
            calendar = it
            updateData()
        }

        binding.viewPagerTimeTableFragment.currentItem = GetDateRepositoryImpl(calendar).getDayOfWeek()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Установка даты на месяц
    private fun setDate(){
        binding.monthDate.text = GetDateRepositoryImpl(calendar).monthAndDayNow()
    }

    // Установка дат дней недели
    private fun setButtonNumbers(){

        val listOfNumber = GetDateRepositoryImpl(calendar).dayNumberOnButton()

        for(a in 0..5){
            days[a].text = listOfNumber[a]
        }
    }

    // Обновление данных
    private fun updateData(newTime: Int? = null){

        binding.viewPagerTimeTableFragment.currentItem = currentItem

        // newTime позволяет перелистывать недели
        if (newTime != null){
            calendar = GetDateRepositoryImpl(calendar).updateCalendar(newTime)
        }
        val data = vm.dataLiveData.value
        val days = GetDateRepositoryImpl(calendar).getArrayOfWeekDate()
        if (data != null) {
            vm.getTimeTable(newData = data, dayOfWeek = days, mainParam = vm.getMainParam())
        }

        setDate()
        setButtonNumbers()

    }

    companion object{
        private const val PREVIOUS_WEEK = -7
        private const val NEXT_WEEK = 7
    }

}
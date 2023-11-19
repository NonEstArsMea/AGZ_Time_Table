package com.NonEstArsMea.agz_time_table.present

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.databinding.MainLayoutBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    var calendar: Calendar = Calendar.getInstance()

    val constCalendar: Calendar = Calendar.getInstance()

    private var _binding: MainLayoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var analytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        analytics = Firebase.analytics

        val viewModelFactory = MainViewModelFactory(this)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        mainViewModel.setCalendar(calendar = calendar)

        mainViewModel.getDataFromStorage()
        // Установка пикера
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        //Нажатие на пикер
        datePicker.addOnPositiveButtonClickListener {
            val calender = Calendar.getInstance()
            calender.timeInMillis = it

            mainViewModel.setNewCalendar(calender.get(Calendar.YEAR),
                              calender.get(Calendar.MONTH),
                                calender.get(Calendar.DAY_OF_MONTH))
            findNavController(R.id.fragmentContainerView)
                    .navigate(R.id.timeTableFragment)
        }



        binding.bottomInfo.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.menu_tt -> {
                    mainViewModel.setCalendar(constCalendar)
                    popBackStack()
                    findNavController(R.id.fragmentContainerView)
                        .navigate(R.id.timeTableFragment)
                    //vm.setNewCalendar(year_n, month_n, day_n)
                }


                R.id.menu_set_date -> {
                    datePicker.show(supportFragmentManager, "DatePicker")
                }

                R.id.menu_setting ->{

                    findNavController(R.id.fragmentContainerView)
                        .navigate(R.id.settingFragment)

                }


            }
            return@setOnItemSelectedListener true
        }

        if (DataRepositoryImpl.isInternetConnected(this)) {
            mainViewModel.loadDataFromURL()
        }



    }



    private fun popBackStack() {
        findNavController(R.id.fragmentContainerView).popBackStack(R.id.timeTableFragment,
            inclusive = true
        )
    }

    override fun onPause() {
        super.onPause()

        mainViewModel.setDataInStorage()
    }
    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}


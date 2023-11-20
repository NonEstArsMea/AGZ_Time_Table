package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.databinding.MainLayoutBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

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
        mainViewModel.getDataFromStorage()




        binding.bottomInfo.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_tt -> {
                    DateRepositoryImpl.setDayNow()
                    findNavController(R.id.fragmentContainerView).popBackStack(
                        R.id.timeTableFragment,
                        inclusive = false
                    )
                }


                R.id.menu_setting -> {

                    findNavController(R.id.fragmentContainerView)
                        .navigate(R.id.settingFragment)

                }


            }
            return@setOnItemSelectedListener true
        }
        Log.e("Is Connected", "internet is connected")
        mainViewModel.loadDataFromURL()
        mainViewModel.isStartLoad.observe(this) {
            if (DataRepositoryImpl.isInternetConnected(this)) {
                Log.e("Is Connected", "internet is connected")
                mainViewModel.loadDataFromURL()
            }
        }


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


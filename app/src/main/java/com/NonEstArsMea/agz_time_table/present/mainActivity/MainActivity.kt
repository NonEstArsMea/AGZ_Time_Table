package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.databinding.MainLayoutBinding
import com.NonEstArsMea.agz_time_table.present.customDateView.CastomDateFragment
import com.NonEstArsMea.agz_time_table.present.settingFragment.SettingFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import java.util.Calendar


class MainActivity : AppCompatActivity(), CastomDateFragment.OnStartAndFinishListener, SettingFragment.setThemeInterface{

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

        mainViewModel.theme.observe(this){
            setCustomTheme(it)
        }


    }

    override fun onStart() {
        super.onStart()
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
        mainViewModel.loadDataFromURL()
        mainViewModel.isStartLoad.observe(this) {
            if (DataRepositoryImpl.isInternetConnected(this)) {
                mainViewModel.loadDataFromURL()
            }
        }
    }

    override fun startFragment() {
        binding.bottomInfo.isGone = true
    }

    override fun closeFragment() {
        binding.bottomInfo.isGone = false
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.setDataInStorage()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    override fun setLightTheme() {
        setCustomTheme(LIGHT_THEME)
    }

    override fun setDarkTheme() {
        setCustomTheme(NIGHT_THEME)
    }

    override fun setSystemTheme() {
        setCustomTheme(SYSTEM_THEME)
    }

    fun setCustomTheme(themeNumber: Int){
        when(themeNumber){
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            3 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> throw RuntimeException("Unknown number of theme")
        }
    }

    companion object{
        private const val SYSTEM_THEME = 1
        private const val NIGHT_THEME = 2
        private const val LIGHT_THEME = 3
    }
}


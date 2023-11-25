package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.os.Bundle
import android.text.Layout.Directions
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.NonEstArsMea.agz_time_table.NavGraphDirections
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.databinding.MainLayoutBinding
import com.NonEstArsMea.agz_time_table.present.customDateFragment.CastomDateFragment
import com.NonEstArsMea.agz_time_table.present.examsFragment.ExamsFragment
import com.NonEstArsMea.agz_time_table.present.settingFragment.SettingFragment
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.TimeTableFragmentDirections
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import androidx.activity.addCallback
import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl


class MainActivity : AppCompatActivity(),
    CastomDateFragment.OnStartAndFinishListener,
    SettingFragment.setThemeInterface,
    ExamsFragment.OnStartAndFinishListener{

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
            mainViewModel.setCustomTheme(it)
        }


    }

    override fun onStart() {
        super.onStart()
        val menu = binding.bottomInfo.menu
        mainViewModel.selectedItem.observe(this){
            Log.e("selectedItem", it.toString())
            menu.getItem(it).isChecked = true

        }

        binding.bottomInfo.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_tt -> {
                    DateRepositoryImpl.setDayNow()
                    findNavController(R.id.fragmentContainerView).popBackStack(
                        R.id.timeTableFragment,
                        inclusive = false
                    )
                }

                R.id.menu_exams -> {
                    findNavController(R.id.fragmentContainerView)
                        .navigate(NavGraphDirections.actionGlobalExamsFragment(
                                mainViewModel.getMainParam()
                            )
                        )
                }

                R.id.menu_setting -> {
                    if(StateRepositoryImpl.stateNow() != StateRepositoryImpl.SETTING_ITEM){
                        findNavController(R.id.fragmentContainerView)
                            .navigate(R.id.settingFragment)
                    }
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
        mainViewModel.setCustomTheme(MainViewModel.LIGHT_THEME)
    }

    override fun setDarkTheme() {
        mainViewModel.setCustomTheme(MainViewModel.NIGHT_THEME)
    }

    override fun setSystemTheme() {
        mainViewModel.setCustomTheme(MainViewModel.SYSTEM_THEME)
    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        return super.getOnBackInvokedDispatcher()
    }


}


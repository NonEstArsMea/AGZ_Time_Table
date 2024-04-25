package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.NonEstArsMea.agz_time_table.NavGraphDirections
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.NonEstArsMea.agz_time_table.databinding.MainLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.customDateFragment.CustomDateFragment
import com.NonEstArsMea.agz_time_table.present.examsFragment.ExamsFragment
import com.NonEstArsMea.agz_time_table.present.settingFragment.SettingFragment
import com.NonEstArsMea.agz_time_table.present.settingFragment.ThemeController
import com.NonEstArsMea.agz_time_table.util.BottomMenuItemStateManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject


class MainActivity : AppCompatActivity(),
    CustomDateFragment.OnStartAndFinishListener,
    SettingFragment.setThemeInterface,
    ExamsFragment.OnStartAndFinishListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var analytics: FirebaseAnalytics

    private var _binding: MainLayoutBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private val component by lazy {
        (application as TimeTableApplication).component
    }

    @Inject
    lateinit var themeController: ThemeController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics

        component.inject(this)
        Log.e("fin", "onCreate")
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]


        _binding = MainLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    override fun onStart() {
        super.onStart()
        mainViewModel.checkNetConnection()
        mainViewModel.isConnected.observe(this){
            // появление кнопки об отсутсвии интернета
            if (!it){
                        binding.errorNetLayout.root.visibility = View.VISIBLE
            }else{
                val collapseAnimation = ObjectAnimator.ofPropertyValuesHolder(
                    binding.errorNetLayout.root,
                    PropertyValuesHolder.ofFloat(View.ALPHA, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f)
                )
                collapseAnimation.duration = 300
                collapseAnimation.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.errorNetLayout.root.visibility = View.GONE
                    }
                })
                collapseAnimation.start()
            }

        }

        binding.errorNetLayout.errorNetLayoutButton.setOnClickListener {
            mainViewModel.checkNetConnection()
        }

        val menu = binding.bottomInfo.menu
        mainViewModel.selectedItem.observe(this) {
            menu.getItem(it).isChecked = true

        }

        mainViewModel.dataIsLoad.observe(this){
            mainViewModel.getListOfMainParam()
        }

        binding.bottomInfo.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_tt -> {
                    DateManager.setDayNow()
                    findNavController(R.id.fragmentContainerView).popBackStack(
                        R.id.timeTableFragment,
                        inclusive = false
                    )
                }

                R.id.menu_exams -> {
                    findNavController(R.id.fragmentContainerView)
                        .navigate(
                            NavGraphDirections.actionGlobalExamsFragment(
                                mainViewModel.getMainParam()
                            )
                        )
                }

                R.id.menu_setting -> {
                    if(mainViewModel.selectedItem.value != BottomMenuItemStateManager.SETTING_ITEM){
                        findNavController(R.id.fragmentContainerView)
                            .navigate(R.id.settingFragment)
                    }

                }


            }
            return@setOnItemSelectedListener true
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
        themeController.setTheme(true, ThemeController.LIGHT_THEME)
    }

    override fun setDarkTheme() {
        themeController.setTheme(true, ThemeController.NIGHT_THEME)
    }

    override fun setSystemTheme() {
        themeController.setTheme(true, ThemeController.SYSTEM_THEME)
    }


}


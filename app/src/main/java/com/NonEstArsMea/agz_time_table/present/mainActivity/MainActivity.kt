package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.NonEstArsMea.agz_time_table.NavGraphDirections
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.AuthRepositoryImpl
import com.NonEstArsMea.agz_time_table.databinding.MainLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.examsFragment.ExamsFragment
import com.NonEstArsMea.agz_time_table.util.BottomMenuItemStateManager
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject


class MainActivity : AppCompatActivity(),
    ExamsFragment.OnStartAndFinishListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var analytics: FirebaseAnalytics

    private var _binding: MainLayoutBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    @Inject
    lateinit var userAuth: AuthRepositoryImpl

    private val component by lazy {
        (application as TimeTableApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        component.inject(this)

        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        mainViewModel.startApp()
        mainViewModel.theme.observe(this) { themeNumber ->
            when (themeNumber) {
                SYSTEM_THEME -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )

                NIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )

                LIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )

                else -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
            }
            this.window.setWindowAnimations(R.style.ThemeChangeAnimation)

        }

        this.window.setWindowAnimations(R.style.ThemeChangeAnimation)
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

        userAuth.init(this)

        _binding = MainLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    override fun onStart() {
        super.onStart()
        binding.errorNetLayout.errorNetLayoutButton.setOnClickListener {
            //mainViewModel.checkNetConnection()
        }

        val menu = binding.bottomInfo.menu
        mainViewModel.selectedItem.observe(this) {
            menu.getItem(it).isChecked = true

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
                    if (mainViewModel.selectedItem.value != BottomMenuItemStateManager.SETTING_ITEM) {
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

    companion object {
        const val SYSTEM_THEME = 1
        const val NIGHT_THEME = 2
        const val LIGHT_THEME = 3
    }

}


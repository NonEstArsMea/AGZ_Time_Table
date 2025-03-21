package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.NonEstArsMea.agz_time_table.NavGraphDirections
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.databinding.MainLayoutBinding
import com.NonEstArsMea.agz_time_table.present.TimeTableApplication
import com.NonEstArsMea.agz_time_table.present.examsFragment.ExamsFragment
import com.NonEstArsMea.agz_time_table.util.BottomMenuItemStateManager
import com.NonEstArsMea.agz_time_table.util.DateManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject


/**
 * Основная активность приложения, которая управляет навигацией, темой и состоянием приложения.
 * Реализует интерфейс OnStartAndFinishListener для взаимодействия с фрагментами.
 */
class MainActivity : AppCompatActivity(), ExamsFragment.OnStartAndFinishListener {

    private lateinit var mainViewModel: MainViewModel // ViewModel для управления состоянием активности
    private lateinit var analytics: FirebaseAnalytics // Аналитика Firebase для отслеживания событий

    private var _binding: MainLayoutBinding? = null // Привязка макета активности
    private val binding get() = _binding!! // Геттер для безопасного доступа к привязке

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory // Фабрика для создания ViewModel через Dagger

    /**
     * Ленивая инициализация Dagger-компонента из Application.
     */
    private val component by lazy {
        (application as TimeTableApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Внедрение зависимостей через Dagger
        component.inject(this)

        // Инициализация ViewModel
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        mainViewModel.startApp()

        // Наблюдение за изменением темы и применение выбранной темы
        mainViewModel.theme.observe(this) { themeNumber ->
            when (themeNumber) {
                SYSTEM_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                NIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                LIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            this.window.setWindowAnimations(R.style.ThemeChangeAnimation)
        }

        super.onCreate(savedInstanceState)

        // Инициализация Firebase Analytics
        analytics = Firebase.analytics

        // Привязка макета активности
        _binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        // Обработка клика по кнопке в layout ошибки сети
        binding.errorNetLayout.errorNetLayoutButton.setOnClickListener {
            setClickable()
        }

        // Наблюдение за выбранным элементом в нижнем меню и его обновление
        val menu = binding.bottomInfo.menu
        mainViewModel.selectedItem.observe(this) {
            menu.getItem(it).isChecked = true
        }

        setClickable() // Установка видимости элементов в зависимости от состояния

        // Обработка выбора элементов в нижнем меню
        binding.bottomInfo.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_tt -> {
                    DateManager.setDayNow()
                    findNavController(R.id.fragmentContainerView).popBackStack(R.id.timeTableFragment, inclusive = false)
                }
                R.id.menu_exams -> {
                    findNavController(R.id.fragmentContainerView).navigate(
                        NavGraphDirections.actionGlobalExamsFragment(mainViewModel.getMainParam())
                    )
                }
                R.id.menu_setting -> {
                    if (mainViewModel.selectedItem.value != BottomMenuItemStateManager.SETTING_ITEM) {
                        val navOptions = navOptions {
                            anim {
                                enter = R.anim.slide_in_right
                                exit = R.anim.slide_out_left
                                popEnter = R.anim.slide_in_left
                                popExit = R.anim.slide_out_right
                            }
                        }
                        findNavController(R.id.fragmentContainerView).navigate(
                            NavGraphDirections.actionToSettingFragment(), navOptions
                        )
                    }
                }
            }
            true
        }
    }

    /**
     * Устанавливает видимость элементов в зависимости от состояния сети.
     */
    fun setClickable() {
        val visible = mainViewModel.checkClickable()
        binding.bottomInfo.menu.forEachIndexed { index, item ->
            item.isVisible = visible
        }
        binding.errorNetLayout.root.isVisible = !visible
    }

    /**
     * Скрывает нижнее меню при запуске фрагмента.
     */
    override fun startFragment() {
        binding.bottomInfo.isGone = true
    }

    /**
     * Показывает нижнее меню при закрытии фрагмента.
     */
    override fun closeFragment() {
        binding.bottomInfo.isGone = false
    }


    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.setDataInStorage() // Сохранение данных при паузе
        _binding = null // Очистка привязки для предотвращения утечек памяти
    }

    companion object {
        const val SYSTEM_THEME = 1 // Константа для системной темы
        const val NIGHT_THEME = 2 // Константа для темной темы
        const val LIGHT_THEME = 3 // Константа для светлой темы
    }
}


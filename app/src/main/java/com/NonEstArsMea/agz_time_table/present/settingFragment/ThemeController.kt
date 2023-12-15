package com.NonEstArsMea.agz_time_table.present.settingFragment

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.State.ChangeThemeUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import javax.inject.Inject

class ThemeController @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository
) {
    fun checkTheme(): Int {
        return when (timeTableRepositoryImpl.getTheme().value) {
            1 -> R.id.button1
            2 -> R.id.button2
            3 -> R.id.button3
            else -> {
                R.id.button1
            }
        }
    }

    fun setTheme(isChecked: Boolean, checkedId: Int){
        if (isChecked) {
            Log.e("fin", "theme")
            when (checkedId) {
                R.id.button1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    timeTableRepositoryImpl.setTheme(SYSTEM_THEME)
                }
                R.id.button2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    timeTableRepositoryImpl.setTheme(NIGHT_THEME)
                }
                R.id.button3 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    timeTableRepositoryImpl.setTheme(LIGHT_THEME)
                }
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    companion object{
        const val SYSTEM_THEME = 1
        const val NIGHT_THEME = 2
        const val LIGHT_THEME = 3
    }
}
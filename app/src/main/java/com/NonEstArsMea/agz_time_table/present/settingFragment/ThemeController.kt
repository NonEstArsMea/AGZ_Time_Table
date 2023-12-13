package com.NonEstArsMea.agz_time_table.present.settingFragment

import android.util.Log
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
                R.id.button1 -> timeTableRepositoryImpl.setTheme(ChangeThemeUseCase.LIGHT_THEME)
                R.id.button2 -> timeTableRepositoryImpl.setTheme(ChangeThemeUseCase.NIGHT_THEME)
                R.id.button3 -> timeTableRepositoryImpl.setTheme(ChangeThemeUseCase.SYSTEM_THEME)
            }
        }
    }
}
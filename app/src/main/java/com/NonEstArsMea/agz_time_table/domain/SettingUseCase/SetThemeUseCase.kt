package com.NonEstArsMea.agz_time_table.domain.SettingUseCase

import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.State.ChangeThemeUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModel
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(private val repository: TimeTableRepository) {

    fun execute(isChecked: Boolean, checkedId: Int){
        if (isChecked) {
            when (checkedId) {
                R.id.button1 -> TimeTableRepositoryImpl.setTheme(ChangeThemeUseCase.LIGHT_THEME)
                R.id.button2 -> TimeTableRepositoryImpl.setTheme(ChangeThemeUseCase.NIGHT_THEME)
                R.id.button3 -> TimeTableRepositoryImpl.setTheme(ChangeThemeUseCase.SYSTEM_THEME)
            }
        }
    }
}
package com.NonEstArsMea.agz_time_table.present.settingFragment

import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.TimeTableRepository
import javax.inject.Inject

class CheckTheme{
    fun execute(): Int{
        return when (TimeTableRepositoryImpl.getTheme().value) {
            1 -> R.id.button1
            2 -> R.id.button2
            3 -> R.id.button3
            else -> {
                R.id.button1
            }
        }
    }
}
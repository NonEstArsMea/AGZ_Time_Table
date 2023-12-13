package com.NonEstArsMea.agz_time_table.domain.mainUseCase.State

import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject

class ChangeThemeUseCase @Inject constructor() {

    fun execute(themeNumber: Int){
        when(themeNumber){
            SYSTEM_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            NIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            LIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> throw RuntimeException("Unknown number of theme")
        }
    }

    companion object{
        const val SYSTEM_THEME = 1
        const val NIGHT_THEME = 2
        const val LIGHT_THEME = 3
    }

}
package com.NonEstArsMea.agz_time_table.present.settingFragment

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeController @Inject constructor() {

    private var theme = MutableLiveData<Int>()

    fun getTheme(): LiveData<Int> {
        return theme
    }

    fun setTheme(newTheme: Int) {
        theme.value = newTheme
        changeTheme(newTheme)
    }

    fun checkTheme(): Int {
        return when (theme.value) {
            SYSTEM_THEME -> R.id.button1
            NIGHT_THEME -> R.id.button2
            LIGHT_THEME -> R.id.button3
            else -> {
                R.id.button1
            }
        }
    }

    fun setTheme(isChecked: Boolean, checkedId: Int) {
        if (isChecked) {
            Log.e("fin", "theme")
            when (checkedId) {
                R.id.button1 -> {
                    setTheme(SYSTEM_THEME)
                }

                R.id.button2 -> {
                    setTheme(NIGHT_THEME)
                }

                R.id.button3 -> {
                    setTheme(LIGHT_THEME)
                }

                else -> setTheme(SYSTEM_THEME)
            }
        }
    }

    private fun changeTheme(themeNumber: Int) {
        when (themeNumber) {
            SYSTEM_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            NIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            LIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> throw RuntimeException("Unknown number of theme")
        }
    }

    companion object {
        const val SYSTEM_THEME = 1
        const val NIGHT_THEME = 2
        const val LIGHT_THEME = 3
    }
}
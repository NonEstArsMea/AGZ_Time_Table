package com.NonEstArsMea.agz_time_table.present.settingFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModel

class SettingViewModel : ViewModel() {

    // хранит список с главными параметрами
    private val _listOfFavoriteMainParam = TimeTableRepositoryImpl.getArrayOfFavoriteMainParam()
    val listOfFavoriteMainParam: LiveData<ArrayList<MainParam>>
        get() = _listOfFavoriteMainParam

    fun moveItemInFavoriteMainParam(param: MainParam) {
        val list = _listOfFavoriteMainParam.value!!.toList() as ArrayList
        with(list) {
            if (this.size != 1) {
                val itemIndex = this.indexOf(param)
                for (item in itemIndex downTo 1) {
                    this[item] = this[item - 1]
                }
                this[0] = param
            }
            _listOfFavoriteMainParam.value = list
        }
    }

    fun getTheme(): Int {
        return when (TimeTableRepositoryImpl.getTheme().value) {
            1 -> R.id.button1
            2 -> R.id.button2
            3 -> R.id.button3
            else -> {
                R.id.button1
            }
        }
    }

    fun delParamFromFavoriteMainParam(index: MainParam) {
        val items = _listOfFavoriteMainParam.value!!
        items.removeAt(_listOfFavoriteMainParam.value!!.indexOf(index))
        _listOfFavoriteMainParam.value = items
    }

    fun setTheme(isChecked: Boolean, checkedId: Int){
        if (isChecked) {
            when (checkedId) {
                R.id.button1 -> TimeTableRepositoryImpl.setTheme(MainViewModel.LIGHT_THEME)
                R.id.button2 -> TimeTableRepositoryImpl.setTheme(MainViewModel.NIGHT_THEME)
                R.id.button3 -> TimeTableRepositoryImpl.setTheme(MainViewModel.SYSTEM_THEME)
            }
        }
    }

    fun setMainParam(mainParam: MainParam) {
        TimeTableRepositoryImpl.setMainParam(mainParam)
    }

    fun startFragment(){
        StateRepositoryImpl.setNewMenuItem(StateRepositoryImpl.SETTING_ITEM)
    }
}
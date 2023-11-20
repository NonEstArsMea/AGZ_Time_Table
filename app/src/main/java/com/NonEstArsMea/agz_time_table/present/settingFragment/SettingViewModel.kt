package com.NonEstArsMea.agz_time_table.present.settingFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

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


    fun delParamFromFavoriteMainParam(index: MainParam) {
        val items = _listOfFavoriteMainParam.value!!
        items.removeAt(_listOfFavoriteMainParam.value!!.indexOf(index))
        _listOfFavoriteMainParam.value = items
    }

    fun setMainParam(mainParam: MainParam) {
        TimeTableRepositoryImpl.setMainParam(mainParam)
    }
}
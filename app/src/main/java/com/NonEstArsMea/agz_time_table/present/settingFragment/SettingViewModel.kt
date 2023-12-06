package com.NonEstArsMea.agz_time_table.present.settingFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.State.SetSettingItemUseCase
import com.NonEstArsMea.agz_time_table.domain.SettingUseCase.GetArrayOfFavoriteMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.SettingUseCase.GetThemeUseCase
import com.NonEstArsMea.agz_time_table.domain.SettingUseCase.SetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.SettingUseCase.SetThemeUseCase
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModel
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    getArrayOfFavoriteMainParam: GetArrayOfFavoriteMainParamUseCase,
    private val getTheme: GetThemeUseCase,
    private val setTheme: SetThemeUseCase,
    private val setMainParam: SetMainParamUseCase,
    private val setSettingItem: SetSettingItemUseCase
) : ViewModel() {

    // хранит список с главными параметрами
    private val _listOfFavoriteMainParam = getArrayOfFavoriteMainParam.execute()
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
        return getTheme.execute()
    }

    fun delParamFromFavoriteMainParam(index: MainParam) {
        val items = _listOfFavoriteMainParam.value!!
        items.removeAt(_listOfFavoriteMainParam.value!!.indexOf(index))
        _listOfFavoriteMainParam.value = items
    }

    fun setTheme(isChecked: Boolean, checkedId: Int){
        setTheme.execute(isChecked, checkedId)
    }

    fun setMainParam(mainParam: MainParam) {
        setMainParam.execute(mainParam)
    }

    fun startFragment(){
        setSettingItem.execute()
    }
}
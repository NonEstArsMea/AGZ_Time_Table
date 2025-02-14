package com.NonEstArsMea.agz_time_table.present.settingFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.util.BottomMenuItemStateManager
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository,
) : ViewModel() {
    
    private val _listOfFavoriteMainParam = timeTableRepositoryImpl.getArrayOfFavoriteMainParam()
    val listOfFavoriteMainParam: LiveData<ArrayList<MainParam>>
        get() = _listOfFavoriteMainParam

    fun moveItemInFavoriteMainParam(param: MainParam) {
        _listOfFavoriteMainParam.value = timeTableRepositoryImpl.moveItemInFavoriteMainParam(param)
    }

    fun delParamFromFavoriteMainParam(index: MainParam) {
        val items = _listOfFavoriteMainParam.value
        if (items != null) {
            _listOfFavoriteMainParam.value?.let { items.removeAt(it.indexOf(index)) }
        }
        _listOfFavoriteMainParam.value = items
    }

    fun setMainParam(mainParam: MainParam) {
        timeTableRepositoryImpl.setMainParam(mainParam)
    }

    fun startFragment() {
        BottomMenuItemStateManager.setNewMenuItem(BottomMenuItemStateManager.SETTING_ITEM)
    }



}
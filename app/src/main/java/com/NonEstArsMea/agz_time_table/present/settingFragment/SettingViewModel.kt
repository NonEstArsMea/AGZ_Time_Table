package com.NonEstArsMea.agz_time_table.present.settingFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.data.AuthRepositoryImpl
import com.NonEstArsMea.agz_time_table.util.BottomMenuItemStateManager
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.present.loginLayout.LoginViewModel
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val authRepositoryImpl: AuthRepositoryImpl
) : ViewModel() {


    // хранит список с главными параметрами
    private val _listOfFavoriteMainParam = timeTableRepositoryImpl.getArrayOfFavoriteMainParam()
    val listOfFavoriteMainParam: LiveData<ArrayList<MainParam>>
        get() = _listOfFavoriteMainParam

    private val _authResult = authRepositoryImpl.getUserProfile()
    val authResult: LiveData<AuthRepositoryImpl.UserProfile> get() = _authResult

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


    fun logOut() {
        authRepositoryImpl.logout()
    }


}
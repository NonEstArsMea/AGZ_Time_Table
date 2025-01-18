package com.NonEstArsMea.agz_time_table.present.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.util.BottomMenuItemStateManager
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.NetUtil
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.GetDataFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.SetDataInStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.present.settingFragment.ThemeController
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val setDataInStorage: SetDataInStorageUseCase,
    private val getNameParam: GetMainParamUseCase,
    private val getDataFromStorage: GetDataFromStorageUseCase,
    themeController: ThemeController,
    private val timeTableRepositoryImpl: TimeTableRepository,
) : ViewModel() {



    private var _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _isConnected

    private var _theme = themeController.getTheme()
    val theme: LiveData<Int>
        get() = _theme

    private var _selectedItem: MutableLiveData<Int> = BottomMenuItemStateManager.getMenuItem()
    val selectedItem: LiveData<Int>
        get() = _selectedItem




    init {
        getDataFromStorage.execute()
    }


    fun getMainParam(): String {
        return getNameParam.getNameOfMainParamFromStorage()
    }


    fun setDataInStorage() {
        setDataInStorage.execute(
            mainParam = getNameParam.getLiveData().value,
            favMainParamList = timeTableRepositoryImpl.getArrayOfFavoriteMainParam().value,
            theme = _theme.value,
        )


    }

    fun itemControl(): Boolean {
        return BottomMenuItemStateManager.stateNow() != BottomMenuItemStateManager.SETTING_ITEM
    }





}


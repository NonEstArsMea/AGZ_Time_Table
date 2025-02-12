package com.NonEstArsMea.agz_time_table.present.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.GetDataFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.SetDataInStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.util.BottomMenuItemStateManager
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val setDataInStorage: SetDataInStorageUseCase,
    private val getNameParam: GetMainParamUseCase,
    private val getDataFromStorage: GetDataFromStorageUseCase,
    private val timeTableRepositoryImpl: TimeTableRepository,
) : ViewModel() {

    private var _theme = MutableLiveData<Int>()

    val theme: LiveData<Int>
        get() = _theme

    private var _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _isConnected


    private var _selectedItem: MutableLiveData<Int> = BottomMenuItemStateManager.getMenuItem()
    val selectedItem: LiveData<Int>
        get() = _selectedItem


    init {
        getDataFromStorage.execute()
        _theme.value = getDataFromStorage.getTheme()
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

    fun setTheme(isChecked: Boolean, newTheme: Int) {
        if (isChecked) {
            _theme.value = when (newTheme) {
                R.id.button1 -> MainActivity.SYSTEM_THEME
                R.id.button2 -> MainActivity.NIGHT_THEME
                R.id.button3 -> MainActivity.LIGHT_THEME
                else -> {
                    MainActivity.SYSTEM_THEME
                }
            }
        }

    }

    fun checkTheme(): Int {
        return when (_theme.value) {
            MainActivity.SYSTEM_THEME -> R.id.button1
            MainActivity.NIGHT_THEME -> R.id.button2
            MainActivity.LIGHT_THEME -> R.id.button3
            else -> {
                R.id.button1
            }
        }
    }

    fun setUserTheme(): Int {
        return getDataFromStorage.getTheme()
    }
}


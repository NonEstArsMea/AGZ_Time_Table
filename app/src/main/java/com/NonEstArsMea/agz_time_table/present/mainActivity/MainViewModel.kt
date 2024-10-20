package com.NonEstArsMea.agz_time_table.present.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.data.storage.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.util.BottomMenuItemStateManager
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.DataRepository
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.IsInternetConnected
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.GetDataFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.SetDataInStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.present.settingFragment.ThemeController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val setDataInStorage: SetDataInStorageUseCase,
    private val loadData: DataRepository,
    private val getNameParam: GetMainParamUseCase,
    private val getDataFromStorage: GetDataFromStorageUseCase,
    private val isInternetConnected: IsInternetConnected,
    themeController: ThemeController,
    private val timeTableRepositoryImpl: TimeTableRepository,
) : ViewModel() {


    private val uiScope = viewModelScope

    private var _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _isConnected

    private var _theme = themeController.getTheme()
    val theme: LiveData<Int>
        get() = _theme

    private var _selectedItem: MutableLiveData<Int> = BottomMenuItemStateManager.getMenuItem()
    val selectedItem: LiveData<Int>
        get() = _selectedItem

    private var isReady = false

    val dataIsLoad: LiveData<Boolean> = loadData.dataIsLoad()


    init {
        getDataFromStorage.execute()
    }

    // закгрузка данных и сохраниение
    fun loadDataFromURL() {
        if (!isReady) {
            uiScope.launch(Dispatchers.IO) {
                try {
                    loadData.loadData()
                    isReady = true
                } catch (e: Exception) {
                    delay(1000)
                    loadDataFromURL()
                }
            }
        }
    }

    fun getMainParam(): String {
        return getNameParam.getNameOfMainParamFromStorage()
    }


    fun setDataInStorage() {
        setDataInStorage.execute(
            mainParam = getNameParam.getLiveData().value,
            favMainParamList = timeTableRepositoryImpl.getArrayOfFavoriteMainParam().value,
            theme = _theme.value,
            list = timeTableRepositoryImpl.getArrayOfWeekTimeTable().value
        )


    }

    fun itemControl(): Boolean {
        return BottomMenuItemStateManager.stateNow() != BottomMenuItemStateManager.SETTING_ITEM
    }

    fun checkNetConnection(){
        _isConnected.value = isInternetConnected()
        if(isInternetConnected()){
            loadDataFromURL()
        }
    }

    fun getListOfMainParam() {
        viewModelScope.launch {
            timeTableRepositoryImpl.getListOfMainParam()
        }
    }

    private fun isInternetConnected(): Boolean {
        return isInternetConnected.execute()
    }

}


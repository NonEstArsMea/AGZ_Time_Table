package com.NonEstArsMea.agz_time_table.present.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.data.StateManager
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.DataRepository
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.IsInternetConnected
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.GetDataFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.SetDataInStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetWeekTimeTableListUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.present.settingFragment.ThemeController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val setDataInStorage: SetDataInStorageUseCase,
    private val loadData: DataRepository,
    private val getNameParam: GetMainParamUseCase,
    private val getDataFromStorage: GetDataFromStorageUseCase,
    private val isInternetConnected: IsInternetConnected,
    themeController: ThemeController,
    private val timeTableRepositoryImpl: TimeTableRepository
) : ViewModel() {


    private val uiScope = viewModelScope

    private var _isStartLoad = MutableLiveData<Unit>()
    val isStartLoad: LiveData<Unit>
        get() = _isStartLoad

    private var _theme = themeController.getTheme()
    val theme: LiveData<Int>
        get() = _theme

    private var _selectedItem: MutableLiveData<Int> = StateManager.getMenuItem()
    val selectedItem: LiveData<Int>
        get() = _selectedItem

    private var isReady = false

    val dataIsLoad: LiveData<Boolean> = loadData.dataIsLoad()


    init {
        if(isInternetConnected()){
            loadDataFromURL()
        }
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
            getNameParam.getLiveData().value,
            timeTableRepositoryImpl.getArrayOfFavoriteMainParam().value,
            _theme.value
        )
    }

    fun itemControl(): Boolean {
        return StateManager.stateNow() != StateManager.SETTING_ITEM
    }

    fun isInternetConnected(): Boolean {
        return isInternetConnected.execute()
    }

    fun getListOfMainParam() {
        viewModelScope.launch {
            timeTableRepositoryImpl.getListOfMainParam()
        }
    }

}


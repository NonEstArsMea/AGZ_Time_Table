package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.DataRepository
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.IsInternetConnected
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.GetDataFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.SetDataInStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.settingUseCase.GetArrayOfFavoriteMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.GetWeekTimeTableListUseCase
import com.NonEstArsMea.agz_time_table.present.settingFragment.ThemeController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val setDataInStorage: SetDataInStorageUseCase,
    private val loadData: DataRepository,
    private val getNameParam: GetMainParamUseCase,
    private val getArrayOfFavoriteMainParam: GetArrayOfFavoriteMainParamUseCase,
    private val getArrayOfWeekTimeTable: GetWeekTimeTableListUseCase,
    private val getDataFromStorage: GetDataFromStorageUseCase,
    private val isInternetConnected: IsInternetConnected,
    themeController: ThemeController
) : ViewModel() {


    private val uiScope = viewModelScope

    private var _isStartLoad = MutableLiveData<Unit>()
    val isStartLoad: LiveData<Unit>
        get() = _isStartLoad

    private var _theme = themeController.getTheme()
    val theme: LiveData<Int>
        get() = _theme

    private var _selectedItem: MutableLiveData<Int> = StateRepositoryImpl.getMenuItem()
    val selectedItem: LiveData<Int>
        get() = _selectedItem

    private var isReady = false


    init {
        loadDataFromURL()
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
                    _isStartLoad.postValue(Unit)
                }
            }
        }
    }

    fun getMainParam(): String {
        return getNameParam.getNameOfMainParam()
    }


    fun setDataInStorage() {
        setDataInStorage.execute(
            getNameParam.execute().value,
            getArrayOfFavoriteMainParam.execute().value,
            getArrayOfWeekTimeTable.getArrayOfWeekTimeTable().value,
            _theme.value
        )
    }


    fun itemControl(): Boolean {
        return StateRepositoryImpl.stateNow() != StateRepositoryImpl.SETTING_ITEM
    }

    fun isInternetConnected(): Boolean {
        return isInternetConnected.execute()
    }

}


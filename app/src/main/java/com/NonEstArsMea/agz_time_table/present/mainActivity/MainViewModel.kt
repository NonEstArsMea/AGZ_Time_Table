package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.IsInternetConnected
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.LoadDataUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.State.ChangeThemeUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetDataFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetFavoriteMainParamsFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetLastWeekFromeStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetMainParamFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetThemeFromStorage
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.SetDataInStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.SettingUseCase.GetArrayOfFavoriteMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetWeekTimeTableListUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val setDataInStorage: SetDataInStorageUseCase,
    private val loadData: LoadDataUseCase,
    private val getNameParam: GetMainParamUseCase,
    private val getArrayOfFavoriteMainParam : GetArrayOfFavoriteMainParamUseCase,
    private val getArrayOfWeekTimeTable: GetWeekTimeTableListUseCase,
    private val changeTheme: ChangeThemeUseCase,
    private val getDataFromStorage: GetDataFromStorageUseCase,
    private val isInternetConnected: IsInternetConnected
) : ViewModel() {


    private var jobVM = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + jobVM)

    private var _isStartLoad = MutableLiveData<Unit>()
    val isStartLoad: LiveData<Unit>
        get() = _isStartLoad

    private var _theme = TimeTableRepositoryImpl.getTheme()
    val theme: LiveData<Int>
        get() = _theme

    private var _selectedItem: MutableLiveData<Int> = StateRepositoryImpl.getMenuItem()
    val selectedItem: LiveData<Int>
        get() = _selectedItem

    var isReady = false

    // закгрузка данных и сохраниение
    fun loadDataFromURL() {
        uiScope.launch(Dispatchers.IO) {
            try {
                loadData.execute()
                isReady = true
            } catch (e: Exception) {
                _isStartLoad.postValue(Unit)
            }
        }
    }

    fun getMainParam():String{
        return getNameParam.getNameOfMainParam()
    }

    fun getDataFromStorage() {
        getDataFromStorage.execute()
    }

    fun setDataInStorage() {
        setDataInStorage.execute(
            getNameParam.execute().value,
            getArrayOfFavoriteMainParam.execute().value,
            getArrayOfWeekTimeTable.getArrayOfWeekTimeTable().value,
            _theme.value
        )
    }

    fun setCustomTheme(themeNumber: Int){
        changeTheme.execute(themeNumber)
    }

    fun itemControl():Boolean{
        return  StateRepositoryImpl.stateNow() != StateRepositoryImpl.SETTING_ITEM
    }

    fun isInternetConnected(): Boolean{
        return isInternetConnected.execute()
    }

}
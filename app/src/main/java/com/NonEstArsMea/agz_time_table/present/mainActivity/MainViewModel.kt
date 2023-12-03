package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.LoadDataUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetFavoriteMainParamsFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetLastWeekFromeStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetMainParamFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetThemeFromStorage
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.SetDataInStorageUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val setDataInStorage: SetDataInStorageUseCase,
    private val loadData: LoadDataUseCase,
    private val getMainParamFromStorage: GetMainParamFromStorageUseCase,
    private val getFavoriteMainParamsFromStorageUseCase: GetFavoriteMainParamsFromStorageUseCase,
    private val getLastWeekTimeTableFromStorage: GetLastWeekFromeStorageUseCase,
    private val getThemeFromStorage: GetThemeFromStorage
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

    private val _menuItem = MutableLiveData<Int>()

    // закгрузка данных и сохраниение
    fun loadDataFromURL() {
        uiScope.launch(Dispatchers.IO) {
            try {
                loadData.execute()
            } catch (e: Exception) {
                _isStartLoad.postValue(Unit)
            }
        }
    }

    fun getMainParam():String{
        return TimeTableRepositoryImpl.getMainParam().value?.name.toString()
    }

    fun getDataFromStorage() {
        TimeTableRepositoryImpl.setMainParam(getMainParamFromStorage.execute())
        TimeTableRepositoryImpl.setWeekTimeTable(getLastWeekTimeTableFromStorage.execute())
        TimeTableRepositoryImpl.setListOfFavoriteMainParam(getFavoriteMainParamsFromStorageUseCase.execute())
        TimeTableRepositoryImpl.setTheme(getThemeFromStorage.execute())

    }

    fun setDataInStorage() {
        setDataInStorage.execute(
            TimeTableRepositoryImpl.getMainParam().value,
            TimeTableRepositoryImpl.getArrayOfFavoriteMainParam().value,
            TimeTableRepositoryImpl.getArrayOfWeekTimeTable().value,
            _theme.value
        )
    }

    fun setCustomTheme(themeNumber: Int){
        when(themeNumber){
            SYSTEM_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            NIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            LIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> throw RuntimeException("Unknown number of theme")
        }
    }
    companion object{
        const val SYSTEM_THEME = 1
        const val NIGHT_THEME = 2
        const val LIGHT_THEME = 3
    }
}
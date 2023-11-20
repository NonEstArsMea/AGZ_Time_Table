package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.LoadDataUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetFavoriteMainParamsFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetLastWeekFromeStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetMainParamFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.SetDataInStorageUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainViewModel(
    private val setDataInStorage: SetDataInStorageUseCase,
    private val loadData: LoadDataUseCase,
    private val getMainParamFromStorage: GetMainParamFromStorageUseCase,
    private val getFavoriteMainParamsFromStorageUseCase: GetFavoriteMainParamsFromStorageUseCase,
    private val getLastWeekTimeTableFromStorage: GetLastWeekFromeStorageUseCase,
) : ViewModel() {


    private var jobVM = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + jobVM)

    private var _isStartLoad = MutableLiveData<Unit>()
    val isStartLoad: LiveData<Unit>
        get() = _isStartLoad

    private val _menuItem = MutableLiveData<Int>()

    // закгрузка данных и сохраниение
    fun loadDataFromURL() {
        uiScope.launch(Dispatchers.IO) {
            try {
                loadData.execute()
            } catch (e: Exception) {
                _isStartLoad.postValue(Unit)
                Log.e("my-tag", e.toString())
            }
        }
    }

    fun isNewItem(item: Int): Boolean {
        return if (_menuItem.value != item) {
            _menuItem.value = R.id.menu_tt
            true
        } else
            false

    }

    fun getDataFromStorage() {
        TimeTableRepositoryImpl.setMainParam(getMainParamFromStorage.execute())
        TimeTableRepositoryImpl.setWeekTimeTable(getLastWeekTimeTableFromStorage.execute())
        TimeTableRepositoryImpl.setListOfFavoriteMainParam(getFavoriteMainParamsFromStorageUseCase.execute())
    }

    fun setDataInStorage() {
        setDataInStorage.execute(
            TimeTableRepositoryImpl.getMainParam().value,
            TimeTableRepositoryImpl.getArrayOfFavoriteMainParam().value,
            TimeTableRepositoryImpl.getArrayOfWeekTimeTable().value
        )
    }
}
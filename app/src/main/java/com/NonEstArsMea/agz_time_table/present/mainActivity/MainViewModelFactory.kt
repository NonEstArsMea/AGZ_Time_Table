package com.NonEstArsMea.agz_time_table.present.mainActivity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.LoadDataUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetFavoriteMainParamsFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetLastWeekFromeStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetMainParamFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.SetDataInStorageUseCase

class MainViewModelFactory(context: Context) : ViewModelProvider.Factory {

    /**
     * Work with Storage
     * */

    private val storageRepository = StorageRepositoryImpl(context = context)
    private val setDataInStorage = SetDataInStorageUseCase(storageRepository)
    private val getFavoriteMainParamsFromStorageUseCase =
        GetFavoriteMainParamsFromStorageUseCase(storageRepository)
    private val getLastWeekTimeTable = GetLastWeekFromeStorageUseCase(storageRepository)
    private val getMainParamFromStorage = GetMainParamFromStorageUseCase(storageRepository)

    /**
    Work with Network
     */
    private val dataReposytory = DataRepositoryImpl
    private val loadData = LoadDataUseCase(dataReposytory)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            setDataInStorage = setDataInStorage,
            loadData = loadData,
            getMainParamFromStorage = getMainParamFromStorage,
            getFavoriteMainParamsFromStorageUseCase = getFavoriteMainParamsFromStorageUseCase,
            getLastWeekTimeTableFromStorage = getLastWeekTimeTable,
        ) as T
    }
}
package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.NonEstArsMea.agz_time_table.data.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetLastWeekFromeStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.GetMainParamFromStorageUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetWeekTimeTableListUseCase

class TimeTableViewModelFactory(context: Context): ViewModelProvider.Factory {


    private val repository = TimeTableRepositoryImpl
    private val getWeekTimeTable = GetWeekTimeTableListUseCase(repository, context)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TimeTableViewModel(
            getWeekTimeTableUseCase = getWeekTimeTable,
        ) as T
    }
}
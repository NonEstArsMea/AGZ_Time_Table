package com.NonEstArsMea.agz_time_table.present.customDateFragment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.DateRepository
import com.NonEstArsMea.agz_time_table.domain.GetTimeTableUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.GetDateUseCase

class CustomDateFragmentViewModelFactory(context: Context): ViewModelProvider.Factory {

    private val dateRepository = DateRepositoryImpl

    private val getTimeTableUseCase = GetTimeTableUseCase(context)
    private val getDateUseCase = GetDateUseCase(dateRepository, context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CustomDateFragmentViewModel(
            getTimeTableUseCase,
            getDateUseCase
        ) as T
    }
}
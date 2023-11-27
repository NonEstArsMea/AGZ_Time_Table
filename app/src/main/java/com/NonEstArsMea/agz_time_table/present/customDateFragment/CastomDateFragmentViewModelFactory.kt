package com.NonEstArsMea.agz_time_table.present.customDateFragment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.NonEstArsMea.agz_time_table.domain.GetTimeTableUseCase

class CastomDateFragmentViewModelFactory(context: Context): ViewModelProvider.Factory {

    private val getTimeTableUseCase = GetTimeTableUseCase(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CastomDateFragmentViewModel(
            getTimeTableUseCase
        ) as T
    }
}
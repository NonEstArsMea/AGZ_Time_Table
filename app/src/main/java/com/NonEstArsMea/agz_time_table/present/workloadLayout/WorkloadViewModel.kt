package com.NonEstArsMea.agz_time_table.present.workloadLayout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.StorageRepository
import com.NonEstArsMea.agz_time_table.present.cafTimeTable.CafTimeTableState
import com.NonEstArsMea.agz_time_table.present.cafTimeTable.CafTimeTableViewModel
import javax.inject.Inject

class WorkloadViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel() {

    private var _state = MutableLiveData<CafTimeTableState>()
    val state: LiveData<CafTimeTableState>
        get() = _state
    private var name = "Выберите перподавателя"

    init {
        name = storageRepository.getNameTeacherWorkload()
        Log.e("log", name)
    }

}
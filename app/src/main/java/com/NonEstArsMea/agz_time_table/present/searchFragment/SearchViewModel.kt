package com.NonEstArsMea.agz_time_table.present.searchFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.NonEstArsMea.agz_time_table.data.storage.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.StorageRepository
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.present.cafTimeTable.CafTimeTableViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private var _listOfMainParam = timeTableRepositoryImpl.getNewListOfMainParam()
    val listOfMainParam: LiveData<ArrayList<MainParam>>
        get() = _listOfMainParam

    init {
        timeTableRepositoryImpl.clearListOfMainParam()
    }


    fun setNewMainParam(mainParam: MainParam) {
        timeTableRepositoryImpl.updateFavoriteParamList(mainParam)
        timeTableRepositoryImpl.setMainParam(mainParam)
        storageRepository.setMainParamInStorage(mainParam)
    }

    fun getListOfMainParam(key: Int){
        viewModelScope.launch {
            when(key){
                CafTimeTableViewModel.GROUP_LIST_KEY -> timeTableRepositoryImpl.getListOfGroups()
                CafTimeTableViewModel.TEACHER_LIST_KEY -> timeTableRepositoryImpl.getListOfTeachers()
                CafTimeTableViewModel.TEACHER_LIST_KEY_FOR_WORKLOAD -> timeTableRepositoryImpl.getListOfTeachers()
            }
        }
    }

}
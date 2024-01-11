package com.NonEstArsMea.agz_time_table.present.searchFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository
) : ViewModel() {

    private var _listOfMainParam = timeTableRepositoryImpl.getNewListOfMainParam()
    val listOfMainParam: LiveData<ArrayList<MainParam>>
        get() = _listOfMainParam

    fun setNewMainParam(mainParam: MainParam) {
        timeTableRepositoryImpl.updateFavoriteParamList(mainParam)
        timeTableRepositoryImpl.setMainParam(mainParam)
    }
}
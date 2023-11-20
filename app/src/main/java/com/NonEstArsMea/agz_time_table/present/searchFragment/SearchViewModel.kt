package com.NonEstArsMea.agz_time_table.present.searchFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

class SearchViewModel : ViewModel() {

    private var _listOfMainParam = TimeTableRepositoryImpl.getNewListOfMainParam()
    val listOfMainParam: LiveData<ArrayList<MainParam>>
        get() = _listOfMainParam

    fun setNewMainParam(mainParam: MainParam) {
        TimeTableRepositoryImpl.updateFavoriteParamList(mainParam)
        TimeTableRepositoryImpl.setMainParam(mainParam)
    }
}
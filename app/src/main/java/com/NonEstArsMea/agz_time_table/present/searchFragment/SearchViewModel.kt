package com.NonEstArsMea.agz_time_table.present.searchFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.SettingUseCase.SetMainParamUseCase
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.UpdateFavoriteParamListUseCase
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val setMainParam: SetMainParamUseCase,
    private val updateFavoriteParamListUseCase: UpdateFavoriteParamListUseCase,
    private val timeTableRepositoryImpl: TimeTableRepository
): ViewModel() {

    private var _listOfMainParam = timeTableRepositoryImpl.getNewListOfMainParam()
    val listOfMainParam: LiveData<ArrayList<MainParam>>
        get() = _listOfMainParam

    fun setNewMainParam(mainParam: MainParam) {
        updateFavoriteParamListUseCase.execute(mainParam)
        setMainParam.execute(mainParam)
    }
}
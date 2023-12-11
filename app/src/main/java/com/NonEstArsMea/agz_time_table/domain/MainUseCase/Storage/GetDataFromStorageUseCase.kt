package com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage

import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.TimeTableRepository
import javax.inject.Inject

class GetDataFromStorageUseCase @Inject constructor(
    private val getMainParamFromStorage: GetMainParamFromStorageUseCase,
    private val getLastWeekTimeTableFromStorage: GetLastWeekFromeStorageUseCase,
    private val getFavoriteMainParamsFromStorageUseCase: GetFavoriteMainParamsFromStorageUseCase,
    private val getThemeFromStorage: GetThemeFromStorage,
) {

    fun execute(){
        TimeTableRepositoryImpl.setMainParam(getMainParamFromStorage.execute())
        TimeTableRepositoryImpl.setWeekTimeTable(getLastWeekTimeTableFromStorage.execute())
        TimeTableRepositoryImpl.setListOfFavoriteMainParam(getFavoriteMainParamsFromStorageUseCase.execute())
        TimeTableRepositoryImpl.setTheme(getThemeFromStorage.execute())
    }

}
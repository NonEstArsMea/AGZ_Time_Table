package com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage

import android.util.Log
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.present.settingFragment.ThemeController
import javax.inject.Inject

class GetDataFromStorageUseCase @Inject constructor(
    private val getMainParamFromStorage: GetMainParamFromStorageUseCase,
    private val getLastWeekTimeTableFromStorage: GetLastWeekFromeStorageUseCase,
    private val getFavoriteMainParamsFromStorageUseCase: GetFavoriteMainParamsFromStorageUseCase,
    private val getThemeFromStorage: GetThemeFromStorage,
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val themeController: ThemeController
) {

    fun execute(){
        timeTableRepositoryImpl.setMainParam(getMainParamFromStorage.execute())
        timeTableRepositoryImpl.setWeekTimeTable(getLastWeekTimeTableFromStorage.execute())
        timeTableRepositoryImpl.setListOfFavoriteMainParam(getFavoriteMainParamsFromStorageUseCase.execute())
        themeController.setTheme(getThemeFromStorage.execute())
    }

}
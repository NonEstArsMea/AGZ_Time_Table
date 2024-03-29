package com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.present.settingFragment.ThemeController
import javax.inject.Inject

class GetDataFromStorageUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val themeController: ThemeController,
    private val repository: StorageRepository
) {


    fun execute(){
        timeTableRepositoryImpl.setMainParam(repository.getMainParamFromStorage())
        //timeTableRepositoryImpl.setWeekTimeTable(mapper.repository.getLastWeekFromDataBase())
        timeTableRepositoryImpl.setListOfFavoriteMainParam(repository.getFavoriteMainParamsFromStorage())
        themeController.setTheme(repository.getThemeFromStorage())
    }

}
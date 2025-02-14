package com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import javax.inject.Inject

class GetDataFromStorageUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val repository: StorageRepository
) {


    fun execute(){
        timeTableRepositoryImpl.setMainParam(repository.getMainParamFromStorage())
        timeTableRepositoryImpl.setListOfFavoriteMainParam(repository.getFavoriteMainParamsFromStorage())
    }

    fun getTheme(): Int{
        return repository.getThemeFromStorage()
    }

}
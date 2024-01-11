package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import androidx.lifecycle.LiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.StorageRepository
import javax.inject.Inject

class GetMainParamUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val storageRepository: StorageRepository
) {
    fun getLiveData(): LiveData<MainParam> {
        return timeTableRepositoryImpl.getMainParam()
    }

    fun getNameOfMainParamFromRepo(): String? {
        return timeTableRepositoryImpl.getMainParam().value?.name
    }

    fun getNameOfMainParamFromStorage():String{
        return storageRepository.getMainParamFromStorage().name
    }
}
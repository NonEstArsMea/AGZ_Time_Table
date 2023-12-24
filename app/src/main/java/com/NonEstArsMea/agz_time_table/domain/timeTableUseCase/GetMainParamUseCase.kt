package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import androidx.lifecycle.LiveData
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.StorageRepository
import javax.inject.Inject

class GetMainParamUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val storageRepository: StorageRepository
) {
    fun execute(): LiveData<MainParam> {
        return timeTableRepositoryImpl.getMainParam()
    }

    fun getNameOfMainParam():String{
        return storageRepository.getMainParamFromStorage().name
    }
}
package com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class GetMainParamFromStorageUseCase @Inject constructor(private val repository: StorageRepository) {
    fun execute():MainParam{
        return repository.getMainParamFromStorage()
    }
}
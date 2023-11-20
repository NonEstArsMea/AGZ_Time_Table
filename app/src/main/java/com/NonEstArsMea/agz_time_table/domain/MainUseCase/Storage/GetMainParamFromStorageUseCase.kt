package com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

class GetMainParamFromStorageUseCase(private val repository: StrotageRepository) {
    fun execute():MainParam{
        return repository.getMainParamFromStorage()
    }
}
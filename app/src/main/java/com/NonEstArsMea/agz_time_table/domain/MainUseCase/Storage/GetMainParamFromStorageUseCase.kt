package com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage

class GetMainParamFromStorageUseCase(private val repository: StrotageRepository) {
    fun execute():String{
        return repository.getMainParamFromStorage()
    }
}
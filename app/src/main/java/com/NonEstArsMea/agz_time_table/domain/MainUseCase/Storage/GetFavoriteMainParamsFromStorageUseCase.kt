package com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

class GetFavoriteMainParamsFromStorageUseCase(private val repository: StrotageRepository) {

    fun execute(): ArrayList<MainParam>{
        return repository.getFavoriteMainParamsFromStorage()
    }
}
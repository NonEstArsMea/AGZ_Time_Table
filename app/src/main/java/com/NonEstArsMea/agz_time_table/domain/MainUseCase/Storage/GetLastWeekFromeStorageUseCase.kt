package com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import javax.inject.Inject

class GetLastWeekFromeStorageUseCase @Inject constructor(private val repository: StorageRepository) {

    fun execute():ArrayList<ArrayList<CellApi>>{
        return repository.getLastWeekFromStorage()
    }
}
package com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.LessonTimeTable

class GetLastWeekFromeStorageUseCase(private val repository: StrotageRepository) {

    fun execute():ArrayList<ArrayList<CellApi>>{
        return repository.getLastWeekFromStorage()
    }
}
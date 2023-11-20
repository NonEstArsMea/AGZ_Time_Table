package com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.StrotageRepository
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam

class SetDataInStorageUseCase(private val repository: StrotageRepository) {
    fun execute(mainParam: MainParam?,
                favMainParamList: ArrayList<MainParam>?,
                lastWeekTimeTable: ArrayList<ArrayList<CellApi>>?){
        return repository.setDataInStorage(mainParam, favMainParamList, lastWeekTimeTable)
    }
}
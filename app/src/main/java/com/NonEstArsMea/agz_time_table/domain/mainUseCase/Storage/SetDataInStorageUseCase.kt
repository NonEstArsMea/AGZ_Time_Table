package com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class SetDataInStorageUseCase @Inject constructor(private val repository: StorageRepository) {
    fun execute(mainParam: MainParam?,
                favMainParamList: ArrayList<MainParam>?,
                lastWeekTimeTable: List<List<CellApi>>?,
                theme: Int?){
        return repository.setDataInStorage(mainParam, favMainParamList, lastWeekTimeTable, theme)
    }
}
package com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class SetDataInStorageUseCase @Inject constructor(private val repository: StorageRepository) {
    fun execute(mainParam: MainParam?,
                favMainParamList: ArrayList<MainParam>?,
                theme: Int?){
        return repository.setDataInStorage(mainParam, favMainParamList, theme)
    }
}
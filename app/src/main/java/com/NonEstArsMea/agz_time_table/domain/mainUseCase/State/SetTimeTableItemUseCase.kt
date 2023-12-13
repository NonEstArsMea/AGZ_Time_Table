package com.NonEstArsMea.agz_time_table.domain.mainUseCase.State

import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
import javax.inject.Inject

class SetTimeTableItemUseCase @Inject constructor() {
    fun execute(){
        StateRepositoryImpl.setNewMenuItem(StateRepositoryImpl.TIME_TABLE_ITEM)
    }
}
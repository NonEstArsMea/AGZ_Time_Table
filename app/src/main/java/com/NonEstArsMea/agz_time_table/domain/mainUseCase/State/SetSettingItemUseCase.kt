package com.NonEstArsMea.agz_time_table.domain.mainUseCase.State

import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
import javax.inject.Inject

class SetSettingItemUseCase @Inject constructor() {

    fun execute(){
        StateRepositoryImpl.setNewMenuItem(StateRepositoryImpl.SETTING_ITEM)
    }
}
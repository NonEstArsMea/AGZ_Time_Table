package com.NonEstArsMea.agz_time_table.domain.MainUseCase.State

import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.StateRepository
import javax.inject.Inject

class SetSettingItemUseCase @Inject constructor(private val repository: StateRepository) {

    fun execute(){
        repository.setNewMenuItem(StateRepositoryImpl.SETTING_ITEM)
    }
}
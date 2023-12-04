package com.NonEstArsMea.agz_time_table.domain.MainUseCase.State

import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
import javax.inject.Inject

class SetTimeTableItemUseCase @Inject constructor(private val repository: StateRepository) {
    fun execute(){
        repository.setNewMenuItem(StateRepositoryImpl.TIME_TABLE_ITEM)
    }
}
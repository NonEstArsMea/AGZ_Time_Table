package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import javax.inject.Inject

class GetListOfMainParam @Inject constructor(private val repository: TimeTableRepository) {
    fun execute(){
        repository.getListOfMainParam()
    }
}
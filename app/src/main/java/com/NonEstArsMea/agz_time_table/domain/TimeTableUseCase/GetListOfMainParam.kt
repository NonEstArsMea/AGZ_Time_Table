package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

class GetListOfMainParam(private val repository: TimeTableRepository) {
    fun execute(){
        repository.getListOfMainParam()
    }
}
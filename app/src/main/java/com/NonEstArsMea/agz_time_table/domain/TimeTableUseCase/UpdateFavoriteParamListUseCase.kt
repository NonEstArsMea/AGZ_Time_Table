package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class UpdateFavoriteParamListUseCase @Inject constructor(private val repository: TimeTableRepository) {

    fun execute(mainParam: MainParam){
        repository.updateFavoriteParamList(mainParam)
    }
}
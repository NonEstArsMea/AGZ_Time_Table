package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class UpdateFavoriteParamListUseCase @Inject constructor() {

    fun execute(mainParam: MainParam){
        TimeTableRepositoryImpl.updateFavoriteParamList(mainParam)
    }
}
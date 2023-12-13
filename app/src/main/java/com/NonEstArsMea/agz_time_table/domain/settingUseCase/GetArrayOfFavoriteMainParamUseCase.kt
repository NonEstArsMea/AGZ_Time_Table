package com.NonEstArsMea.agz_time_table.domain.settingUseCase

import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class GetArrayOfFavoriteMainParamUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository
) {

    fun execute(): MutableLiveData<ArrayList<MainParam>> {
        return timeTableRepositoryImpl.getArrayOfFavoriteMainParam()
    }
}
package com.NonEstArsMea.agz_time_table.domain.SettingUseCase

import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.TimeTableRepository
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import javax.inject.Inject

class GetArrayOfFavoriteMainParamUseCase @Inject constructor() {

    fun execute(): MutableLiveData<ArrayList<MainParam>> {
        return TimeTableRepositoryImpl.getArrayOfFavoriteMainParam()
    }
}
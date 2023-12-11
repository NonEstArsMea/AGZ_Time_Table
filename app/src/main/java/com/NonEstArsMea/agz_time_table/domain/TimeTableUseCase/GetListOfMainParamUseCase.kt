package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import javax.inject.Inject

class GetListOfMainParamUseCase @Inject constructor(private val context: Context) {
    fun execute(){
        return TimeTableRepositoryImpl.getListOfMainParam(context)
    }
}
package com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase

import android.content.Context
import javax.inject.Inject

class GetListOfMainParamUseCase @Inject constructor(
    private val context: Context,
    private val timeTableRepositoryImpl: TimeTableRepository
) {
    fun execute() {
        return timeTableRepositoryImpl.getListOfMainParam(context)
    }
}
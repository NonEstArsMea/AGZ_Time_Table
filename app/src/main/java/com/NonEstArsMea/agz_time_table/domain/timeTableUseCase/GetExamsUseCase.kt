package com.NonEstArsMea.agz_time_table.domain.timeTableUseCase

import android.content.Context
import android.util.Log
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.net.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import javax.inject.Inject

class GetExamsUseCase @Inject constructor(
    private val timeTableRepositoryImpl: TimeTableRepository,
    private val dataRepositoryImpl: DataRepositoryImpl,
    private val context: Context
) {

    suspend fun execute(mainParam: String): List<CellApi> {
        Log.e("exams", dataRepositoryImpl.getContent().isNotEmpty().toString())
        return if(dataRepositoryImpl.getContent().isNotEmpty() or
            (mainParam != context.getString(R.string.name_param_is_null)))
            timeTableRepositoryImpl.getExams(mainParam)
        else
            listOf()
    }
}
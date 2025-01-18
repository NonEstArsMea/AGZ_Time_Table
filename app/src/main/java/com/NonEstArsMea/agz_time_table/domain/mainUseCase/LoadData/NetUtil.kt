package com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData

import android.content.Context


interface NetUtil {

    fun isInternetConnected(context: Context): Boolean
}
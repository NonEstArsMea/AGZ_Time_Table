package com.NonEstArsMea.agz_time_table.domain.mainUseCase

import android.content.Context
import androidx.lifecycle.MutableLiveData


interface NetUtil {

    fun checkNetConn()

    fun isNetConnection() : Boolean

    fun getNetLiveData() : MutableLiveData<Boolean>
}
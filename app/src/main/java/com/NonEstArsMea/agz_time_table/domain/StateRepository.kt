package com.NonEstArsMea.agz_time_table.domain

import androidx.lifecycle.MutableLiveData

interface StateRepository {

    fun getMenuItem(): MutableLiveData<Int>

    fun setNewMenuItem(newItemId: Int)

    fun stateNow(): Int


}
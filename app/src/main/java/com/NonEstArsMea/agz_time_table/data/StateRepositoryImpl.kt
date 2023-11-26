package com.NonEstArsMea.agz_time_table.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.R

object StateRepositoryImpl {

    private val menuItem = MutableLiveData<Int>().apply {
        this.value = TIME_TABLE_ITEM
    }


    fun getMenuItem(): MutableLiveData<Int>{
        return menuItem
    }

    fun setNewMenuItem(newItemId: Int){
        menuItem.value = newItemId
    }

    fun stateNow(): Int{
        return menuItem.value!!
    }

    const val TIME_TABLE_ITEM = 0
    const val EXAMS_ITEM = 1
    const val SETTING_ITEM = 2

}
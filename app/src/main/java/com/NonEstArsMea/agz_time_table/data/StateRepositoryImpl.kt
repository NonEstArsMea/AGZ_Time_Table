package com.NonEstArsMea.agz_time_table.data

import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.State.StateRepository

object StateRepositoryImpl: StateRepository {

    private val menuItem = MutableLiveData<Int>().apply {
        this.value = TIME_TABLE_ITEM
    }


    override fun getMenuItem(): MutableLiveData<Int>{
        return menuItem
    }

    override fun setNewMenuItem(newItemId: Int){
        menuItem.value = newItemId
    }

    override fun stateNow(): Int{
        return menuItem.value!!
    }

    const val TIME_TABLE_ITEM = 0
    const val EXAMS_ITEM = 1
    const val SETTING_ITEM = 2

}
package com.NonEstArsMea.agz_time_table.data.storage

import android.app.Application
import com.NonEstArsMea.agz_time_table.data.dataBase.CellClassDao
import com.NonEstArsMea.agz_time_table.data.dataBase.DataBase

class DataBaseInitial(
    private val application: Application
) {
    fun getDataBase(): CellClassDao {
        return DataBase.getInstance(application).cellClassDao()
    }
}
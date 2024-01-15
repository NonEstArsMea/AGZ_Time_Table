package com.NonEstArsMea.agz_time_table.data.dataBase

import androidx.room.TypeConverter
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CellClassListConverter {

    @TypeConverter
    fun convertFromString(value: String): List<CellClass> {

        val listType = object : TypeToken<List<CellClass>>() {}.type
        return Gson().fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun convertToString(value: List<CellClass>): String {
        return Gson().toJson(value)
    }

}
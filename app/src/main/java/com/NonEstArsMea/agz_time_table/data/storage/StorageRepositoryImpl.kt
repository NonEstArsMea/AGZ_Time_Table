package com.NonEstArsMea.agz_time_table.data.storage

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.data.dataBase.CellClassDbModel
import com.NonEstArsMea.agz_time_table.data.dataBase.CellClassListConverter
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.StorageRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val resource: Resources,
    localStorage: LocalStorageInitial,
    private val dataStorageManager: DataStorageManager,
) : StorageRepository {

    private val sharedPreferences = localStorage.getSharedPreferences()

    override fun getMainParamFromStorage(): MainParam {
        val token = object : TypeToken<MainParam>() {}.type
        return gson.fromJson(
            sharedPreferences.getString(
                MAIN_PARAM_KEY,
                null
            ),
            token
        ) ?: MainParam(resource.getString(R.string.name_param_is_null))
    }

    override fun getFavoriteMainParamsFromStorage(): ArrayList<MainParam> {
        val token = object : TypeToken<ArrayList<MainParam>>() {}.type
        return gson.fromJson(
            sharedPreferences.getString(
                LIST_OF_FAVORITE_MAIN_PARAMS,
                null
            ),
            token
        ) ?: arrayListOf()
    }

    override fun getLastWeekFromStorage(): List<List<CellClass>> {
       val list = dataStorageManager.loadData()
        return list
    }

    override fun setTimeTableInStorage(list: List<List<CellClass>>?) {
        dataStorageManager.saveData(list)
    }

    override fun getThemeFromStorage(): Int {
        return sharedPreferences.getInt(THEME, SYSTEM_THEME)
    }

    override fun setDataInStorage(
        mainParam: MainParam?,
        favMainParamList: ArrayList<MainParam>?,
        theme: Int?
    ) {
        sharedPreferences.edit().apply {
            if (favMainParamList != null)
                putString(LIST_OF_FAVORITE_MAIN_PARAMS, gson.toJson(favMainParamList))
            if (mainParam != null)
                putString(MAIN_PARAM_KEY, gson.toJson(mainParam))
            if (theme != null) {
                putInt(THEME, theme)
            }

        }.apply()

    }

    companion object {
        private const val MAIN_PARAM_KEY = "mainParam"
        private const val LIST_OF_FAVORITE_MAIN_PARAMS = "LOFMP"
        private const val LAST_WEEK_TIME_TABLE_LIST = "LWTTL"
        private const val THEME = "T"
        private const val SYSTEM_THEME = 1

        private val gson: Gson = Gson()
    }
}

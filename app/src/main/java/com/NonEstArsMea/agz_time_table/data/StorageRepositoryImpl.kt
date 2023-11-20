package com.NonEstArsMea.agz_time_table.data

import android.content.Context
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.StrotageRepository
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StorageRepositoryImpl(context: Context) : StrotageRepository {

    private val _context = context
    private val sharedPreferences =
        _context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)


    override fun getMainParamFromStorage(): MainParam {
        val string = sharedPreferences.getString(
            MAIN_PARAM_KEY,
            _context.resources.getString(R.string.name_param_is_null)
        ) ?: _context.resources.getString(R.string.name_param_is_null)
        val token = object : TypeToken<MainParam>() {}.type
        return Gson().fromJson(string, token)
    }

    override fun getFavoriteMainParamsFromStorage(): ArrayList<MainParam> {
        val string = sharedPreferences.getString(
            LIST_OF_FAVORITE_MAIN_PARAMS,
            arrayListOf<MainParam>().toString()
        ) ?: arrayListOf<MainParam>().toString()
        val token = object : TypeToken<ArrayList<MainParam>>() {}.type
        return Gson().fromJson(string, token)
    }

    override fun getLastWeekFromStorage(): ArrayList<ArrayList<CellApi>> {
        val string = sharedPreferences.getString(
            LAST_WEEK_TIME_TABLE_LIST,
            arrayListOf<ArrayList<CellApi>>().toString()
        ) ?: arrayListOf<ArrayList<CellApi>>().toString()
        val token = object : TypeToken<ArrayList<ArrayList<CellApi>>>() {}.type
        return Gson().fromJson(string, token)
    }

    override fun setDataInStorage(
        mainParam: MainParam?,
        favMainParamList: ArrayList<MainParam>?,
        lastWeekTimeTable: ArrayList<ArrayList<CellApi>>?
    ) {
        sharedPreferences.edit().apply {
            if (favMainParamList != null)
                putString(LIST_OF_FAVORITE_MAIN_PARAMS, Gson().toJson(favMainParamList))
            if (mainParam != null)
                putString(MAIN_PARAM_KEY, Gson().toJson(mainParam))
            if (lastWeekTimeTable != null)
                putString(LAST_WEEK_TIME_TABLE_LIST, Gson().toJson(lastWeekTimeTable))

        }.apply()

    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "SPN"
        const val MAIN_PARAM_KEY = "mainParam"
        const val LIST_OF_FAVORITE_MAIN_PARAMS = "LOFMP"
        const val LAST_WEEK_TIME_TABLE_LIST = "LWTTL"
    }
}
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
        val token = object : TypeToken<MainParam>() {}.type
        val string = Gson().fromJson(
            sharedPreferences.getString(
            MAIN_PARAM_KEY,
            null),
            token) ?: MainParam(_context.resources.getString(R.string.name_param_is_null))
        return string
    }

    override fun getFavoriteMainParamsFromStorage(): ArrayList<MainParam> {
        val token = object : TypeToken<ArrayList<MainParam>>() {}.type
        val string = Gson().fromJson(
            sharedPreferences.getString(
            LIST_OF_FAVORITE_MAIN_PARAMS,
            null),
            token) ?: arrayListOf<MainParam>()
        return string
    }

    override fun getLastWeekFromStorage(): ArrayList<ArrayList<CellApi>> {
        val token = object : TypeToken<ArrayList<ArrayList<CellApi>>>() {}.type
        val string = Gson().fromJson(
            sharedPreferences.getString(
            LAST_WEEK_TIME_TABLE_LIST,
            null),
            token) ?: arrayListOf<ArrayList<CellApi>>()

        return string
    }

    override fun getThemeFromStorage(): Int {
        return sharedPreferences.getInt(THEME, SYSTEM_THEME) ?: SYSTEM_THEME
    }

    override fun setDataInStorage(
        mainParam: MainParam?,
        favMainParamList: ArrayList<MainParam>?,
        lastWeekTimeTable: ArrayList<ArrayList<CellApi>>?,
        theme: Int?
    ) {
        sharedPreferences.edit().apply {
            if (favMainParamList != null)
                putString(LIST_OF_FAVORITE_MAIN_PARAMS, Gson().toJson(favMainParamList))
            if (mainParam != null)
                putString(MAIN_PARAM_KEY, Gson().toJson(mainParam))
            if (lastWeekTimeTable != null)
                putString(LAST_WEEK_TIME_TABLE_LIST, Gson().toJson(lastWeekTimeTable))
            if (theme != null)
                putInt(THEME, theme)

        }.apply()

    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "SPN"
        const val MAIN_PARAM_KEY = "mainParam"
        const val LIST_OF_FAVORITE_MAIN_PARAMS = "LOFMP"
        const val LAST_WEEK_TIME_TABLE_LIST = "LWTTL"
        const val THEME = "T"
        private const val SYSTEM_THEME = 1
    }
}
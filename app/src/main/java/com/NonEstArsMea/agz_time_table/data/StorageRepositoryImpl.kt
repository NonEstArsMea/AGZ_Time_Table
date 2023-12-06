package com.NonEstArsMea.agz_time_table.data

import android.content.Context
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.StrotageRepository
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(private val context: Context) : StrotageRepository {

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)


    override fun getMainParamFromStorage(): MainParam {
        val token = object : TypeToken<MainParam>() {}.type
        return GsonInstance.gson.fromJson(
            sharedPreferences.getString(
                MAIN_PARAM_KEY,
                null
            ),
            token
        ) ?: MainParam(context.resources.getString(R.string.name_param_is_null))
    }

    override fun getFavoriteMainParamsFromStorage(): ArrayList<MainParam> {
        val token = object : TypeToken<ArrayList<MainParam>>() {}.type
        return GsonInstance.gson.fromJson(
            sharedPreferences.getString(
                LIST_OF_FAVORITE_MAIN_PARAMS,
                null
            ),
            token
        ) ?: arrayListOf()
    }

    override fun getLastWeekFromStorage(): ArrayList<ArrayList<CellApi>> {
        val token = object : TypeToken<ArrayList<ArrayList<CellApi>>>() {}.type

        return GsonInstance.gson.fromJson(
            sharedPreferences.getString(
                LAST_WEEK_TIME_TABLE_LIST,
                null
            ),
            token
        ) ?: arrayListOf()
    }

    override fun getThemeFromStorage(): Int {
        return sharedPreferences.getInt(THEME, SYSTEM_THEME)
    }

    override fun setDataInStorage(
        mainParam: MainParam?,
        favMainParamList: ArrayList<MainParam>?,
        lastWeekTimeTable: List<List<CellApi>>?,
        theme: Int?
    ) {
        sharedPreferences.edit().apply {
            if (favMainParamList != null)
                putString(LIST_OF_FAVORITE_MAIN_PARAMS, GsonInstance.gson.toJson(favMainParamList))
            if (mainParam != null)
                putString(MAIN_PARAM_KEY, GsonInstance.gson.toJson(mainParam))
            if (lastWeekTimeTable != null)
                putString(LAST_WEEK_TIME_TABLE_LIST, GsonInstance.gson.toJson(lastWeekTimeTable))
            if (theme != null)
                putInt(THEME, theme)

        }.apply()

    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "SPN"
        private const val MAIN_PARAM_KEY = "mainParam"
        private const val LIST_OF_FAVORITE_MAIN_PARAMS = "LOFMP"
        private const val LAST_WEEK_TIME_TABLE_LIST = "LWTTL"
        private const val THEME = "T"
        private const val SYSTEM_THEME = 1
    }
}

object GsonInstance {
    val gson: Gson = Gson()
}
package com.NonEstArsMea.agz_time_table.data.storage

import android.content.res.Resources
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.domain.dataClass.MainParam
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.StorageRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val resource: Resources,
    localStorage: LocalStorageInitial,
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


    override fun getThemeFromStorage(): Int {
        return sharedPreferences.getInt(THEME, SYSTEM_THEME)
    }

    override fun setDataInStorage(
        mainParam: MainParam?,
        favMainParamList: ArrayList<MainParam>?,
        theme: Int?,
        list: List<List<CellClass>>
    ) {
        sharedPreferences.edit().apply {
            if (favMainParamList != null)
                putString(LIST_OF_FAVORITE_MAIN_PARAMS, gson.toJson(favMainParamList))
            if (mainParam != null)
                putString(MAIN_PARAM_KEY, gson.toJson(mainParam))
            if (theme != null) {
                putInt(THEME, theme)
            }
            putString(LIST_KEY, gson.toJson(list))

        }.apply()

    }

    override fun setCafIdInStorage(id: String) {
        sharedPreferences.edit().apply {
            putString(CAF_ID, id)
        }.apply()
    }

    override fun getCafIdInStorage(): String {
        return sharedPreferences.getString(CAF_ID, "").toString()
    }

    override fun getTimeTableFromStorage(): List<List<CellClass>> {
        val token = object : TypeToken<List<List<CellClass>>>() {}.type
        return gson.fromJson(
            sharedPreferences.getString(LIST_KEY, gson.toJson(listOf<List<CellClass>>())),
            token
        ) ?: listOf()
    }

    override fun getNameTeacherWorkload(): String {
        return sharedPreferences.getString(NAME_KEY, ERROR_VALUE) ?: ERROR_VALUE
    }

    override fun setNameTeacherWorkloadInStorage(name: String) {
        sharedPreferences.edit().apply {
            putString(NAME_KEY, name)
        }.apply()
    }

    companion object {
        private const val MAIN_PARAM_KEY = "mainParam"
        private const val LIST_KEY = "LK"
        private const val NAME_KEY = "NK"
        private const val LIST_OF_FAVORITE_MAIN_PARAMS = "LOFMP"
        private const val CAF_ID = "CI"
        private const val THEME = "T"
        private const val SYSTEM_THEME = 1
        const val ERROR_VALUE =  "Выберите преподавателя"

        private val gson: Gson = Gson()
    }
}

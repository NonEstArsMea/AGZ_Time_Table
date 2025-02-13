package com.NonEstArsMea.agz_time_table.data.storage

import android.content.Context
import android.content.SharedPreferences

class LocalStorageInitial(
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getSharedPreferences(): SharedPreferences{
        return sharedPreferences
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "SPN"
    }
}
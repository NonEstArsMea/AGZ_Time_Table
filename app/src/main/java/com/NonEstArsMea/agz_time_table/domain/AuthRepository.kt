package com.NonEstArsMea.agz_time_table.domain

import android.content.Context

interface AuthRepository {

    fun init(context: Context)
    fun logout()
    fun login(name: String, isLoggedIn: Boolean)

    var username: String?

    var isLoggedIn: Boolean

}
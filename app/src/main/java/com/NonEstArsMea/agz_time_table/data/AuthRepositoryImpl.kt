package com.NonEstArsMea.agz_time_table.data

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.NonEstArsMea.agz_time_table.domain.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {


    private val PREFS_NAME = "user_session"
    private val KEY_USERNAME = "username"
    private val KEY_LOGGED_IN = "logged_in"

    private val profile = MutableLiveData<UserProfile>()

    private lateinit var preferences: SharedPreferences

    override fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (isLoggedIn) {
            profile.value = UserProfile.IsLoggedIn(username!!)
        } else {
            profile.value = UserProfile.IsLoggedOut
        }
    }

    override var username: String? = null
        get() = preferences.getString(KEY_USERNAME, null)


    override var isLoggedIn: Boolean = false
        get() = preferences.getBoolean(KEY_LOGGED_IN, false)


    override fun logout() {
        preferences.edit().clear().apply()
        profile.value = UserProfile.IsLoggedOut
    }

    override fun login(name: String, isLoggedIn: Boolean) {
        preferences.edit().putString(KEY_USERNAME, name).apply()
        preferences.edit().putBoolean(KEY_LOGGED_IN, isLoggedIn).apply()
        profile.value = UserProfile.IsLoggedIn(username!!)
    }

    fun getUserProfile(): MutableLiveData<UserProfile> {
        return profile
    }

    sealed class UserProfile {
        data class IsLoggedIn(val name: String) : UserProfile()
        data object IsLoggedOut : UserProfile()
    }

}
package com.NonEstArsMea.agz_time_table.present.loginLayout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.data.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepositoryImpl: AuthRepositoryImpl
) : ViewModel() {

    private val _authResult = MutableLiveData<Result>()
    val authResult: LiveData<Result> get() = _authResult


    fun signIn(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authResult.value = Result.Success
                        authRepositoryImpl.login(username, true)
                    } else {
                        _authResult.value = Result.Error("Неверный логин или пароль")
                    }
                }
        } else {
            _authResult.value = Result.Error("Поля не заполнены")
        }
    }

    sealed class Result {
        data object Success : Result()
        data class Error(val message: String) : Result()
    }

}
package com.NonEstArsMea.agz_time_table.data.net.retrofit.services

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitServieces {

    @GET("unique-values")
    fun getMainParamsList(): Call<MutableList<String>>
}
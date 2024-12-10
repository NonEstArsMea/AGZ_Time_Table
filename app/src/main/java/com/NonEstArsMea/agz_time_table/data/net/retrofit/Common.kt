package com.NonEstArsMea.agz_time_table.data.net.retrofit

import com.NonEstArsMea.agz_time_table.data.net.retrofit.services.RetrofitServieces

object Common {
    private val BASE_URL = "https://agzprogs.ru/api/"
    val retrofitService: RetrofitServieces
        get(){
            return RetrofitClient.getClient(BASE_URL).create(RetrofitServieces::class.java)
        }
}
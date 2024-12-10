package com.NonEstArsMea.agz_time_table.data.net.retrofit.services

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServieces {

    @GET("unique-values")
    suspend fun getMainParamsList(): Response<ArrayList<String>>

    @GET("aggregate")
    suspend fun getAggregate(
        @Query("dayOfWeek") daysOfWeek: List<String>,
        @Query("mainParam") mainParam: String
    ): Response<List<List<CellClass>>>
}
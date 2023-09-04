package com.example.roadkill.api

import com.example.roadkill.ApiClient
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface NearmissService {

    @POST("/reports/nearmiss/{id}")
    fun postNearmissReport(
        @Body jsonParams: NearmissRequest,
        @Path("id") id: String
    ): Call<String>

    companion object{

        fun retrofitPostNearmissReport(jsonParams: NearmissRequest, id: String): Call<String> {
            return ApiClient.create(NearmissService::class.java).postNearmissReport(jsonParams, id)
        }

    }
}
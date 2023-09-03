package com.example.roadkill.api

import com.example.roadkill.ApiClient
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NearmissService {

    @POST("/reports/nearmiss/64ec86f06a0e3e3b3893f0b7")
    fun postNearmissReport(
        @Body jsonParams: NearmissRequest
    ): Call<String>

    companion object{

        fun retrofitPostNearmissReport(jsonParams: NearmissRequest): Call<String> {
            return ApiClient.create(NearmissService::class.java).postNearmissReport(jsonParams)
        }

    }
}
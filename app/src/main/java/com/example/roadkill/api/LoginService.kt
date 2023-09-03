package com.example.roadkill.api

import com.example.roadkill.ApiClient
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("/auth/login")
    fun postLoginReport(
        @Body jsonParams: LoginRequest
    ): Call<String>

    companion object{

        fun retrofitPostLoginReport(jsonParams: LoginRequest): Call<String> {
            return ApiClient.create(LoginService::class.java).postLoginReport(jsonParams)
        }

    }
}
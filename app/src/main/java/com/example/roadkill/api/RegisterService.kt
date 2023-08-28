package com.example.roadkill.api


import RegisterRequest
import RegisterResponse
import com.example.roadkill.ApiClient
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RegisterService {
    @POST("/users/register")
    fun postRegister(
        @Body jsonParams: RegisterRequest
    ): Call<String>

    companion object{

        fun retrofitPostRegister(jsonParams: RegisterRequest): Call<String>{
            return ApiClient.create(RegisterService::class.java).postRegister(jsonParams)
        }

    }
}
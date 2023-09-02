package com.example.roadkill.api

import com.example.roadkill.ApiClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ReportService {
    @Multipart
    @POST("reports/64ec86f06a0e3e3b3893f0b7") // Replace with your API endpoint
    fun postReport(
        @Part img: MultipartBody.Part,
        @Part("lat") lat: Double,
        @Part("lng") lng: Double,
        @Part("species") species: String,
        @Part("cause") cause: String,
        @Part("otherInfo") otherInfo: String,
        @Part("status") status: Boolean
    ): Call<String>
    companion object {
        fun create(): ReportService {
            return ApiClient.create(ReportService::class.java)
        }
    }
}
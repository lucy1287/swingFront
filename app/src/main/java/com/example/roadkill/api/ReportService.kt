package com.example.roadkill.api

import com.example.roadkill.ApiClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ReportService {
    @Multipart
    @POST("reports/64f4103dc49cc764a40a7e06") // Replace with your API endpoint
    fun postReport(
        @Part img: MultipartBody.Part,
        @Part("lat") lat: Double,
        @Part("lng") lng: Double,
        @Part("species") species: String,
        @Part("cause") cause: String,
        @Part("otherInfo") otherInfo: String,
        @Part("status") status: Boolean
    ): Call<String>


    @GET("reports/64f4103dc49cc764a40a7e06")
    fun getMyReport(
    ):Call<List<MyReportResponse>>

    @GET("reports/post/64f5064b4c800865985dc2ca")
    fun getReportDetail(
    ):Call<MyReportResponse>

    //관리자
    @GET("reports")
    fun getManagerReport(
    ):Call<List<ManagerReportResponse>>

    companion object {
        fun create(): ReportService {
            return ApiClient.create(ReportService::class.java)
        }

        fun retrofitGetMyReport(): Call<List<MyReportResponse>> {
            return ApiClient.create(ReportService::class.java).getMyReport()
        }

        fun retrofitGetReportDetail(): Call<MyReportResponse>{
            return ApiClient.create(ReportService::class.java).getReportDetail()
        }

        fun retrofitGetManagerReport(): Call<List<ManagerReportResponse>> {
            return ApiClient.create(ReportService::class.java).getManagerReport()
        }
    }

}
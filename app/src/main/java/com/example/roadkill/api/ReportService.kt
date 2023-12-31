package com.example.roadkill.api

import com.example.roadkill.ApiClient
import com.example.roadkill.MyApplication
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ReportService {
    @Multipart
    @POST("reports/{id}") // Replace with your API endpoint
    fun postReport(
        @Part img: MultipartBody.Part,
        @Part("lat") lat: Double,
        @Part("lng") lng: Double,
        @Part("species") species: String,
        @Part("cause") cause: String,
        @Part("otherInfoByUser") otherInfoByUser: String,
        @Part("status") status: Boolean,
        @Part("accidentTime") accidentTime: String,
        @Path("id") id: String
    ): Call<String>


    @GET("reports/{id}")
    fun getMyReport(
        @Path("id") id: String
    ):Call<List<MyReportResponse>>

    @GET("reports/post/{rid}")
    fun getReportDetail(
        @Path("rid") rid: String
    ):Call<MyReportResponse>

    //관리자
    @GET("reports")
    fun getManagerReport(
    ):Call<List<ManagerReportResponse>>

    @POST("reports/update/{rid}")
    fun postManagerUpdate(
        @Body jsonParams: ManagerUpdateRequest,
        @Path("rid") _id: String
    ):Call<String>

    companion object {
        fun create(): ReportService {
            return ApiClient.create(ReportService::class.java)
        }

        fun retrofitGetMyReport(id: String): Call<List<MyReportResponse>> {
            return ApiClient.create(ReportService::class.java).getMyReport(id)
        }

        fun retrofitGetReportDetail(rid: String): Call<MyReportResponse>{
            return ApiClient.create(ReportService::class.java).getReportDetail(rid)
        }

        fun retrofitGetManagerReport(): Call<List<ManagerReportResponse>> {
            return ApiClient.create(ReportService::class.java).getManagerReport()
        }

        fun retrofitPostManagerUpdate(jsonParams: ManagerUpdateRequest, rid: String): Call<String>{
            return ApiClient.create(ReportService::class.java).postManagerUpdate(jsonParams, rid)
        }
    }

}
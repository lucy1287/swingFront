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
        @Part("otherInfo") otherInfo: String,
        @Part("status") status: Boolean,
        @Path("id") id: String
    ): Call<String>


    @GET("reports/{id}")
    fun getMyReport(
        @Path("id") id: String
    ):Call<List<MyReportResponse>>

    @GET("reports/post/64f5064b4c800865985dc2ca")
    fun getReportDetail(
    ):Call<MyReportResponse>

    //관리자
    @GET("reports")
    fun getManagerReport(
    ):Call<List<ManagerReportResponse>>

    @POST("update/64f5064b4c800865985dc2ca")
    fun postStatusUpdate(
    ):Call<String>

    companion object {
        fun create(): ReportService {
            return ApiClient.create(ReportService::class.java)
        }

        fun retrofitGetMyReport(id: String): Call<List<MyReportResponse>> {
            return ApiClient.create(ReportService::class.java).getMyReport(id)
        }

        fun retrofitGetReportDetail(): Call<MyReportResponse>{
            return ApiClient.create(ReportService::class.java).getReportDetail()
        }

        fun retrofitGetManagerReport(): Call<List<ManagerReportResponse>> {
            return ApiClient.create(ReportService::class.java).getManagerReport()
        }
    }

}
package com.example.roadkill.api

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.http.Part

//ReportService에 정의한 내용으로 대체
//data class ReportRequest(
//    @SerializedName("img") val img: String,
//    @SerializedName("lat") val lat: Double,
//    @SerializedName("lng") val lng: Double,
//    @SerializedName("species") val species: String,
//    @SerializedName("cause") val cause: String?,
//    @SerializedName("otherInfo") val otherInfo: String?,
//    @SerializedName("status") val status: Boolean,
//)

//data class MyReportResponse(
//    @Part var img: MultipartBody.Part,
//    @Part("lat") var lat: Double,
//    @Part("lng") var lng: Double,
//    @Part("species") var species: String,
//    @Part("cause") var cause: String,
//    @Part("otherInfo") var otherInfo: String,
//    @Part("status") var status: Boolean
//)

data class MyReportResponse(
    @SerializedName("_id") val rid: String,
    @SerializedName("img") val img: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("species") val species: String,
    @SerializedName("cause") val cause: String,
    @SerializedName("otherInfo") val otherInfo: String,
    @SerializedName("status") val status: Boolean,
    @SerializedName("accidentTime") val accidentTime: String
)

data class ManagerReportResponse(
    @SerializedName("_id") val rid: String,
    @SerializedName("img") val img: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("species") val species: String,
    @SerializedName("cause") val cause: String,
    @SerializedName("otherInfo") val otherInfo: String,
    @SerializedName("status") val status: Boolean,
    @SerializedName("accidentTime") val accidentTime: String
)
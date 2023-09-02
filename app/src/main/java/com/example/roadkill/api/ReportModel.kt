package com.example.roadkill.api

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class ReportRequest(
    @SerializedName("img") val img: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("species") val species: String,
    @SerializedName("cause") val cause: String?,
    @SerializedName("otherInfo") val otherInfo: String?,
    @SerializedName("status") val status: Boolean,
)

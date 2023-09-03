package com.example.roadkill.api

import com.google.gson.annotations.SerializedName

data class NearmissRequest(
    @SerializedName("size") val size: Int,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
)

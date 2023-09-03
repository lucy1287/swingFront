package com.example.roadkill.api

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("pwd") val pwd: String,
)


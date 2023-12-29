package com.e.bloctap2pay.nfc.model

import com.google.gson.annotations.SerializedName

data class BaseApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val entity: T?,
    @SerializedName("token")
    val token: String?,



)

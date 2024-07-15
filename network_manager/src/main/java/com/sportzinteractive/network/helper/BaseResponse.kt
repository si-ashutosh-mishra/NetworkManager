package com.sportzinteractive.network.helper

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("content")
    val content: T?,
    @SerializedName("status")
    val status: Int?
)
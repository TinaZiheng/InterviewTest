package com.ai.libraryapp.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class HttpResult<T>(
    @Json(name = "code")
    val code: Int,
    @Json(name = "msg")
    val msg: String,
    @Json(name = "data")
    val data: T
)
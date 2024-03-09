package com.ai.libraryapp.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class Book(
    @Json(name = "id")
    val id : Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "remark")
    val remark: String
)

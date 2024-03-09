package com.ai.libraryapp.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class BookApiListResult(
    @Json(name = "count")
    val isEnd: Int = 0,
    @Json(name = "results")
    val results: List<Book> = listOf()
)

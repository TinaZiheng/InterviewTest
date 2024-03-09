package com.ai.libraryapp.data.bean

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class BookApiListResult(
    @Json(name = "count")
    val isEnd: Boolean = false,
    @Json(name = "results")
    val results: List<Book> = listOf()
)

package com.ai.libraryapp.ui.event

import com.ai.libraryapp.data.bean.Book

enum class BookUpdateType {
    UPDATE,
    ADD
}
data class BookUpdateEvent(val type: BookUpdateType, val book: Book)
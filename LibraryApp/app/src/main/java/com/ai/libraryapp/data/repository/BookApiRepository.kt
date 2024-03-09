package com.ai.libraryapp.data.repository

import com.ai.libraryapp.data.ApiResult
import com.ai.libraryapp.data.bean.Book
import com.ai.libraryapp.data.bean.BookApiListResult
import com.ai.libraryapp.data.bean.HttpResult
import kotlinx.coroutines.flow.Flow

interface BookApiRepository {
    suspend fun getBookList(page: Int, size: Int = 20) : HttpResult<BookApiListResult>
    suspend fun addBook(book: Book) : Flow<ApiResult<HttpResult<Book>>>
    suspend fun getBookById(id: Long) : Flow<ApiResult<HttpResult<Book>>>
    suspend fun updateBook(id: Long, book: Book) : Flow<ApiResult<HttpResult<Book>>>
    suspend fun delBook(id: Long): Flow<ApiResult<HttpResult<Any>>>
}
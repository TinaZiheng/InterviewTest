package com.ai.libraryapp.data.repository

import com.ai.libraryapp.data.ApiResult
import com.ai.libraryapp.data.api.BookApiService
import com.ai.libraryapp.data.bean.Book
import com.ai.libraryapp.data.bean.BookApiListResult
import com.ai.libraryapp.data.bean.HttpResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class BookApiRepositoryIml @Inject constructor(
    private val bookApiService: BookApiService
) : BookApiRepository {
    override suspend fun getBookList(
        page: Int,
        size: Int
    ): Flow<ApiResult<HttpResult<BookApiListResult>>> = flow {
        try {
            val result = bookApiService.getBookListApi(page, size)
            emit(ApiResult.Success(result))
        } catch (e: Exception) {
            emit(ApiResult.Error(Throwable(e.message)))
        }
    }

    override suspend fun addBook(book: Book): Flow<ApiResult<HttpResult<Book>>> = flow {
        try {
            val result = bookApiService.addBook(book)
            emit(ApiResult.Success(result))
        } catch (e: Exception) {
            emit(ApiResult.Error(Throwable(e.message)))
        }
    }

    override suspend fun getBookById(id: Long): Flow<ApiResult<HttpResult<Book>>> = flow {
        try {
            val result = bookApiService.getBookById(id)
            emit(ApiResult.Success(result))
        } catch (e: Exception) {
            emit(ApiResult.Error(Throwable(e.message)))
        }
    }

    override suspend fun updateBook(id: Long, book: Book): Flow<ApiResult<HttpResult<Book>>> = flow {
        try {
            val result = bookApiService.updateBook(id, book)
            emit(ApiResult.Success(result))
        } catch (e: Exception) {
            emit(ApiResult.Error(Throwable(e.message)))
        }
    }

    override suspend fun delBook(id: Long): Flow<ApiResult<HttpResult<Any>>> = flow {
        try {
            val result = bookApiService.delBook(id)
            emit(ApiResult.Success(result))
        } catch (e: Exception) {
            emit(ApiResult.Error(Throwable(e.message)))
        }
    }
}
package com.ai.libraryapp.data.api

import com.ai.libraryapp.data.bean.Book
import com.ai.libraryapp.data.bean.BookApiListResult
import com.ai.libraryapp.data.bean.HttpResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {
    @GET("list")
    suspend fun getBookListApi(
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): HttpResult<BookApiListResult>

    @POST("addBook")
    suspend fun addBook(
        @Body book: Book
    ): HttpResult<Book>

    @GET("getBook")
    suspend fun getBookById(
        @Query("id") id: Long
    ): HttpResult<Book>

    @GET("updateBook/{id}")
    suspend fun updateBook(
        @Path("id") id: Long,
        @Body book: Book
    ): HttpResult<Book>

    @DELETE("delBook/{id}")
    suspend fun delBook(
        @Path("id") id: Long
    ): HttpResult<Any>
}
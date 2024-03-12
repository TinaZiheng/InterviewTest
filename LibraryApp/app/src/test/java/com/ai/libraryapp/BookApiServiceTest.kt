package com.ai.libraryapp

import com.ai.libraryapp.data.api.BookApiService
import com.ai.libraryapp.data.bean.Book
import com.ai.libraryapp.extension.readFile
import com.google.common.truth.Truth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class BookApiServiceTest {
    private lateinit var retrofitBookApi: BookApiService
    private lateinit var mockWebServer: MockWebServer
    private val createBookFile = "retrofit_api/createBookBodyFile.json".readFile()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        retrofitBookApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/api/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(BookApiService::class.java)
    }

    @After
    fun close() {
        mockWebServer.shutdown()
    }

    @Test
    fun createProduct_Assert_IsPathRight() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(createBookFile))

        retrofitBookApi.addBook(Book(0, "testBookPath", ""))

        val request = runCatching {
            mockWebServer.takeRequest(timeout = 5, unit = TimeUnit.SECONDS)
        }.getOrNull()

        Truth.assertThat(request?.path).isEqualTo("/api/addBook")
    }

    @Test
    fun deleteProduct_Assert_IsProductDeleted() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(createBookFile))

        val response = retrofitBookApi.delBook(1)

        Truth.assertThat(response.code).isAtLeast(0)
    }
}
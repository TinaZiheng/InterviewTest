package com.ai.libraryapp.di

import android.util.Log
import com.ai.libraryapp.data.api.BookApiService
import com.ai.libraryapp.data.repository.BookApiRepository
import com.ai.libraryapp.data.repository.BookApiRepositoryIml
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitApiModule {

    @Singleton
    @Provides
    fun provideOkHttp() : OkHttpClient = OkHttpClient().newBuilder().addInterceptor(Interceptor { chain ->
        val request: Request = chain.request()
        Log.i("Request URL:", "${request.url}")
        chain.proceed(request)
    }).build()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideBookService(
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ) : BookApiService = Retrofit.Builder()
        .baseUrl("http://192.168.10.2:8080/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()
        .create(BookApiService::class.java)

    @Singleton
    @Provides
    fun provideBookRepository(
        bookApiService: BookApiService
    ): BookApiRepository = BookApiRepositoryIml(
        bookApiService= bookApiService
    )

}
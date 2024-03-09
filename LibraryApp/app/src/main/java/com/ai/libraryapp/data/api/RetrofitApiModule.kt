package com.ai.libraryapp.data.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitApiModule {

    @Singleton
    @Provides
    fun provideOkHttp() : OkHttpClient = OkHttpClient().newBuilder().build()



}
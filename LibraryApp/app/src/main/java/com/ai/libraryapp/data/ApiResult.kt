package com.ai.libraryapp.data

sealed class ApiResult<out R> {
    data class Success<out T> (val data: T) : ApiResult<T>()
    data class Error(val exception: Throwable) : ApiResult<Nothing>()
}
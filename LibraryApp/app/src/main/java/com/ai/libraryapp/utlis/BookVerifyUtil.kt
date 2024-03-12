package com.ai.libraryapp.utlis

object BookVerifyUtil {
    fun isValidBookInput(name: String?): Boolean = name?.isNotEmpty() == true
}
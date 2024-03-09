package com.ai.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.ai.libraryapp.data.repository.BookApiRepository
import javax.inject.Inject

class CreateBookViewModel @Inject constructor(
    private val repository: BookApiRepository
) : ViewModel() {

}
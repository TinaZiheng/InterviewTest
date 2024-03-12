package com.ai.libraryapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.libraryapp.data.ApiResult
import com.ai.libraryapp.data.bean.Book
import com.ai.libraryapp.data.repository.BookApiRepository
import com.ai.libraryapp.ui.event.BookUpdateEvent
import com.ai.libraryapp.ui.event.BookUpdateType
import com.ai.libraryapp.utlis.BookVerifyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject
@HiltViewModel
class CreateBookViewModel @Inject constructor(
    private val repository: BookApiRepository
) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> = _toastMsg

    private val _finishActivity = MutableLiveData<Boolean>()
    val finishActivity: LiveData<Boolean>
        get() = _finishActivity

    private var book: Book? = null

    fun getRememberBook() : Book? {
        return book
    }

    fun updateAndRememberBook(book: Book) {
        this.book = book
    }

    fun submitUpdate() {
        if (!BookVerifyUtil.isValidBookInput(book?.name)) {
            _toastMsg.value = "The name can not be empty"
        } else {
            viewModelScope.launch {
                _loading.value = true
                if (book!!.id > 0) { //Edit
                    repository.updateBook(book!!.id, book!!).collect {result->
                        _loading.value = false
                        when(result) {
                            is ApiResult.Success -> {
                                EventBus.getDefault().post(BookUpdateEvent(BookUpdateType.UPDATE, result.data.data))
                                _toastMsg.value = "Update success"
                                _finishActivity.value = true
                            }
                            is ApiResult.Error -> {
                                _toastMsg.value = "Edit error : ${result.exception}"
                            }
                        }
                    }
                } else { //Add
                    repository.addBook(book!!).collect {result ->
                        _loading.value = false
                        when(result) {
                            is ApiResult.Success -> {
                                EventBus.getDefault().post(BookUpdateEvent(BookUpdateType.ADD, result.data.data))
                                _toastMsg.value = "Add success"
                                _finishActivity.value = true
                            }
                            is ApiResult.Error -> {
                                _toastMsg.value = "Add error : ${result.exception}"
                            }
                        }
                    }
                }
            }
        }
    }
}
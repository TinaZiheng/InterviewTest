package com.ai.libraryapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.ai.libraryapp.data.ApiResult
import com.ai.libraryapp.data.bean.Book
import com.ai.libraryapp.data.repository.BookApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: BookApiRepository
) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> = _toastMsg

    private val _searchBook = MutableLiveData<Book>()
    val searchBook: LiveData<Book> = _searchBook

    val items: MutableList <Book> = mutableStateListOf()

    private val pagerConfig = PagingConfig(
        pageSize = 20,
        initialLoadSize = 20
    )
    val pager by lazy {
        Pager(
            config = pagerConfig,
            initialKey = 0,
            pagingSourceFactory = {
                object : PagingSource<Int, Book>() {
                    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
                        return state.anchorPosition
                    }

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
                        return try {
                            val page = params.key ?: 0
                            val pageSize = params.loadSize
                            val response = repository.getBookList(page, pageSize)
                            val nextPage = if (response.data.isEnd) null else page + 1
                            Log.d("BookViewModel","page=$page size=$pageSize isEnd=${response.data.isEnd}")
                            LoadResult.Page(response.data.results, null, nextPage)
                        } catch (e: Exception) {
                            LoadResult.Error(e)
                        }
                    }
                }
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun deleteProduct(book: Book) = viewModelScope.launch {
        _loading.value = true
        repository.delBook(book.id).collect {result ->
            _loading.value = false
            when(result) {
                is ApiResult.Success -> {
                    items.remove(book)
                    _toastMsg.value = "Delete success"
                    _searchBook.value = null
                }
                is ApiResult.Error -> {
                    _toastMsg.value = "Delete error : ${result.exception.toString()}"
                }
            }

        }
    }

    fun searchBook(searchStr: String) {
        val id = try {
            searchStr.toLong()
        } catch (e: Exception) {
            null
        }
        if (id == null) {
            _toastMsg.value = "Please input a book’s id which is a number"
        } else {
            viewModelScope.launch {
                _loading.value = true
                repository.getBookById(id).collect {result ->
                    _loading.value = false
                    when(result) {
                        is ApiResult.Success -> {
                            _searchBook.value = result.data.data
                        }
                        is ApiResult.Error -> {
                            _toastMsg.value = "Delete error : ${result.exception.toString()}"
                        }
                    }

                }
            }
        }
    }

    fun addBook(book: Book) {
        items.add(0, book)
    }

    fun updateBook(book: Book) {
        val oldBook = items.find { it ->  book.id == it.id}
        oldBook?.name = book.name
        oldBook?.remark = book.remark
    }
}
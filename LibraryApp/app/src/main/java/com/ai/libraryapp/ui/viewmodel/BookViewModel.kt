package com.ai.libraryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.libraryapp.data.ApiResult
import com.ai.libraryapp.data.bean.Book
import com.ai.libraryapp.data.repository.BookApiRepository
import com.ai.libraryapp.ui.state.LoadState
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

    //data list
    private val _items = MutableLiveData<MutableList<Book>>()
    val items: LiveData<MutableList<Book>> get() = _items
    private var currentPage = 0
    private val _loadState = MutableLiveData<LoadState>()
    val loadState: LiveData<LoadState> = _loadState
    private val _isEnd = MutableLiveData<Boolean>()
    val isEnd: LiveData<Boolean> = _isEnd

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        //no more data
        if (currentPage >=1 && isEnd.value == true) {
            return
        }
        if (loadState.value == LoadState.Loading) return

        _loadState.value = LoadState.Loading

        viewModelScope.launch {
            try {
                val response = repository.getBookList(currentPage, 20)
                Log.d("BookViewModel","page=$currentPage isEnd=${response.data?.isEnd}")
                if (response.code >= 0) {
                    if (currentPage == 0) {
                        _items.value = mutableListOf()
                    }
                    _items.value = (_items.value ?: mutableListOf()).apply {
                        response.data?.results?.let {
                            addAll(it)
                        }
                    }
                    _isEnd.value = response.data?.isEnd
                    currentPage++
                    _loadState.value = LoadState.Complete
                } else {
                    _toastMsg.value = "load error"
                    _loadState.value = LoadState.Error
                }
            } catch (e: Exception) {
                _toastMsg.value = "load error:$e"
                _loadState.value = LoadState.Error
            }
        }
    }

//    private val pagerConfig = PagingConfig(
//        pageSize = 20,
//        initialLoadSize = 20
//    )
//    val pager by lazy {
//        Pager(
//            config = pagerConfig,
//            initialKey = 0,
//            pagingSourceFactory = {
//                object : PagingSource<Int, Book>() {
//                    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
//                        return state.anchorPosition
//                    }
//
//                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
//                        return try {
//                            val page = params.key ?: 0
//                            val pageSize = params.loadSize
//                            val response = repository.getBookList(page, pageSize)
//                            val nextPage = if (response.data.isEnd) null else page + 1
//                            Log.d("BookViewModel","page=$page size=$pageSize isEnd=${response.data.isEnd}")
//                            LoadResult.Page(response.data.results, null, nextPage)
//                        } catch (e: Exception) {
//                            LoadResult.Error(e)
//                        }
//                    }
//                }
//            }
//        ).flow.cachedIn(viewModelScope)
//    }

    fun deleteProduct(book: Book) = viewModelScope.launch {
        _loading.value = true
        repository.delBook(book.id).collect {result ->
            _loading.value = false
            when(result) {
                is ApiResult.Success -> {
                    _toastMsg.value = "Delete success"
                    if (_searchBook.value?.id == book.id) {
                        _searchBook.value = null
                    }
                    _items.value?.remove(book)
                }
                is ApiResult.Error -> {
                    _toastMsg.value = "Delete error : ${result.exception}"
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
                            if (result.data.code < 0) {
                                _toastMsg.value = result.data.msg
                            } else {
                                _searchBook.value = result.data.data
                            }
                        }
                        is ApiResult.Error -> {
                            _toastMsg.value = "error : ${result.exception.toString()}"
                        }
                    }

                }
            }
        }
    }

    fun addBook(book: Book) {
        (_items.value ?: mutableListOf()).apply {
            add(0, book)
        }
    }

    fun updateBook(book: Book) {
       (_items.value?.find { it.id == book.id })?.apply {
           name = book.name
           remark = book.remark
       }
    }
}
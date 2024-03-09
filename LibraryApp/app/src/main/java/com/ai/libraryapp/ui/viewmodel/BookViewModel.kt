package com.ai.libraryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.ai.libraryapp.data.bean.Book
import com.ai.libraryapp.data.repository.BookApiRepository
import javax.inject.Inject

class BookViewModel @Inject constructor(
    private val repository: BookApiRepository
) : ViewModel() {

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
                        return 0
                    }

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
                       try {
                           val page = params.key ?: 0
                           val pageSize = params.loadSize
                           val response = repository.getBookList(page, pageSize)
                           if (response.code >= 0) {
                               val previousPage = if (page > 1) page - 1 else null
                               val nextPage = if (response.data.isEnd) null else page + 1
                               Log.d("BookViewModel","page=$page size=$pageSize")
                               LoadResult.Page(response.data.results, previousPage, nextPage)
                           } else {
                               LoadResult.Error<Any, Any>(Error(if (response.msg.isEmpty()) "Unknown exception error code=${response.code}" else response.msg))
                           }
                       } catch (e: Exception) {
                           LoadResult.Error<Any, Any>(e)
                       }
                        return  LoadResult.Invalid()
                    }
                }
            }
        ).flow.cachedIn(viewModelScope)
    }
}
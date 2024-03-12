package com.ai.libraryapp.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ai.libraryapp.data.bean.Book
import com.ai.libraryapp.ui.state.LoadState
import com.ai.libraryapp.ui.viewmodel.BookViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BookList(viewModel: BookViewModel = hiltViewModel(),
             onDelClick: (book: Book)-> Unit,
             onEditClick: (book: Book) -> Unit) {

    val items by viewModel.items.observeAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()
    val loadState by viewModel.loadState.observeAsState(LoadState.Loading)
    val isEnd by viewModel.isEnd.observeAsState(initial = false)

    if (items.isEmpty()) {
        if (loadState == LoadState.Loading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Loading")
            }
        } else if (loadState == LoadState.Error) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Loading Error")
            }
        }
    } else {
        LazyColumn {
            items(count = items.size) { index ->
                val item = items[index]
                bookItem(item, onDelClick, onEditClick)

            }
            if (loadState == LoadState.Loading) {
                item {
                    Text(
                        text = "Loading",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else if (loadState == LoadState.Error) {
                item {
                    Text(
                        text = "Loading Error",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else if (!isEnd) {
                item {
                    coroutineScope.launch {
                        viewModel.loadNextPage()
                    }
                }
            }
        }
    }


//    val datas = viewModel.pager.collectAsLazyPagingItems()
//
//    if (datas.loadState.refresh is LoadState.Loading) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = "Loading")
//        }
//    } else if (datas.loadState.refresh is LoadState.Error) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = "Loading Error")
//        }
//    } else {
//        LazyColumn {
//            items(count = datas.itemCount) {index ->
//                val item = datas[index]
//                bookItem(item, onDelClick, onEditClick)
//            }
//            if (datas.loadState.append is LoadState.Loading) {
//                item {
//                    Text(
//                        text = "Loading",
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 10.dp),
//                        textAlign = TextAlign.Center
//                    )
//                }
//            } else if (datas.loadState.append is LoadState.Error) {
//                item {
//                    Text(
//                        text = "Loading Error",
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 10.dp),
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
//
//    }
}

@Composable
fun bookItem(bookItem: Book?,
             onDelClick: (book: Book)-> Unit,
             onEditClick: (book: Book) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(
                text = "${bookItem?.name}",
                textAlign = TextAlign.Center
            )
            Text(
                text = "${bookItem?.remark}",
                textAlign = TextAlign.Center
            )
        }
        Row {
            Button(onClick = { bookItem?.let { onDelClick(bookItem) } }) {
                Text(text = "Delete")
            }
            Spacer(modifier = Modifier.padding(start = 16.dp))
            Button(onClick = { bookItem?.let { onEditClick(bookItem) } }) {
                Text(text = "Edit")
            }
        }

    }
}

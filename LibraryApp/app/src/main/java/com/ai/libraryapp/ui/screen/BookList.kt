package com.ai.libraryapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.ai.libraryapp.ui.viewmodel.BookViewModel

@Composable
fun BookList(viewModel: BookViewModel = hiltViewModel()) {
    val datas = viewModel.pager.collectAsLazyPagingItems()

    if (datas.loadState.refresh is LoadState.Loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Loading")
        }
    } else if (datas.loadState.refresh is LoadState.Error) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Loading Error")
        }
    } else {
        LazyColumn {
            items(count = datas.itemCount) {index ->
                val bookItem = datas[index]
                Text(
                    text = "Index= ${bookItem?.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    textAlign = TextAlign.Center
                )
            }
            if (datas.loadState.append is LoadState.Loading) {
                item {
                    Text(
                        text = "Loading",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else if (datas.loadState.append is LoadState.Error) {
                item {
                    Text(
                        text = "Loading Error",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }
}
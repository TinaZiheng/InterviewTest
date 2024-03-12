package com.ai.libraryapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ai.libraryapp.ui.event.BookUpdateEvent
import com.ai.libraryapp.ui.event.BookUpdateType
import com.ai.libraryapp.ui.screen.BookList
import com.ai.libraryapp.ui.screen.bookItem
import com.ai.libraryapp.ui.theme.LibraryAppTheme
import com.ai.libraryapp.ui.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }

        initObserve()
    }

    private fun initObserve() {
        viewModel.toastMsg.observe(this) {
            if(it.isNotEmpty()) {
                Toast.makeText(this, it,  Toast.LENGTH_SHORT).show()
            }
        }

        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe
    fun onMessageEvent(event: BookUpdateEvent) {
        Log.d("MainActivity", "Message received: ${event.type}")
        if (event.type == BookUpdateType.ADD) {
            viewModel.addBook(event.book)
        } else {
            viewModel.updateBook(event.book)
        }
    }
}

@Composable
fun LoadingDialog(isLoading: Boolean, text: String) {
//    if (isLoading) {
//        Dialog(onDismissRequest = { }) {
//            Card(
//                shape = RoundedCornerShape(16.dp),
//            ) {
//                Column(verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.padding(30.dp)) {
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .width(32.dp)
//                            .height(32.dp),
//                        color = MaterialTheme.colorScheme.secondary,
//                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
//                    )
//                    Spacer(modifier = Modifier.padding(top = 16.dp))
//                    Text(text = text)
//                }
//            }
//        }
//    }
}

@Composable
fun MainScreen() {
    val viewModel: BookViewModel = viewModel()
    val loading by viewModel.loading.observeAsState(false)
    val context = LocalContext.current

    Scaffold (
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(context, CreateBookActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(Icons.Rounded.Add, contentDescription ="Add" )
            }
    }) {
        Column(
            modifier = Modifier
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SearchBar()
            searchResult()
            BookList(onDelClick = {book ->
                viewModel.deleteProduct(book)
            }, onEditClick = {book ->
                val intent = Intent(context, CreateBookActivity::class.java)
                val extras = Bundle()
                extras.putParcelable("Book", book)
                intent.putExtras(extras)
                context.startActivity(intent)
            })
            LoadingDialog(isLoading = loading, "Deleting")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    val viewModel: BookViewModel = viewModel()
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp)) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { setSearchText(it) },
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
                .padding(vertical = 0.dp),
            keyboardActions = KeyboardActions(onSearch = {
                viewModel.searchBook(searchText)
                focusManager.clearFocus()
            }),
            placeholder = { Text("Search") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.large.copy(CornerSize(5.dp)),
            textStyle = TextStyle(fontSize = 14.sp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {
                viewModel.searchBook(searchText)
                focusManager.clearFocus()
            }
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_search),
                contentDescription = "Search",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun searchResult() {
    val context = LocalContext.current
    val viewModel: BookViewModel = viewModel()
    val item by viewModel.searchBook.observeAsState()
    if (item != null) {
        bookItem(item, {book ->
              viewModel.deleteProduct(book)
        }, {book ->
                val intent = Intent(context, CreateBookActivity::class.java)
                intent.putExtra("Book", book)
                context.startActivity(intent)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LibraryAppTheme {
        MainScreen()
    }
}